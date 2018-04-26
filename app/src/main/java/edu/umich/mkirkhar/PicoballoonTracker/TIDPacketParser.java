package edu.umich.mkirkhar.PicoballoonTracker;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Calendar;
import java.util.zip.CRC32;


class TIDPacketParser
{
    // TID packet member variables
    // constants
    // offsets, sizes of each TID packet element
    private static final int TIDPacketHeaderOffset = 0;
    private static final int TIDPacketHeaderLength = 2;
    private static final int TIDPayloadSizeOffset = 2;
    private static final int TIDPayloadSizeLength = 2;
    private static final int TIDRSSIindBmOffset = 4;
    private static final int TIDRSSIindBmLength = 4;
    private static final int TIDPayloadOffset = 8;
    private static final int TIDPayloadLength = 100;
    private static final int TIDPayloadCRCOffset = 108;
    private static final int TIDPayloadCRCLength = 4;
    // 2 byte padding to force alignment to 4 byte boundary
    private static final int TIDPacketTrailerOffset = 114;
    private static final int TIDPacketTrailerLength = 2;

    private static final int TIDPacketLength = 116;

    // offsets, sizes of each tracker data packet payload
    private static final int TrackerIDOffset = 0;
    private static final int TrackerIDLength = 10;
    private static final int TrackerRadioTemperatureOffset = 10;
    private static final int TrackerRadioTemperatureLength = 2;
    private static final int TrackerMillisecondCountOffset = 12;
    private static final int TrackerMillisecondCountLength = 4;
    private static final int TrackerBatteryMillivolts0Offset = 16;
    private static final int TrackerBatteryMillivolts0Length = 4;
    private static final int TrackerBatteryMilliamps0Offset = 20;
    private static final int TrackerBatteryMilliamps0Length = 4;

    private static final int TrackerBatteryMillivolts1Offset = 24;
    private static final int TrackerBatteryMillivolts1Length = 4;
    private static final int TrackerBatteryMilliamps1Offset = 28;
    private static final int TrackerBatteryMilliamps1Length = 4;

    private static final int TrackerBatteryMillivolts2Offset = 32;
    private static final int TrackerBatteryMillivolts2Length = 4;
    private static final int TrackerBatteryMilliamps2Offset = 36;
    private static final int TrackerBatteryMilliamps2Length = 4;

    private static final int TrackerBatteryMillivolts3Offset = 40;
    private static final int TrackerBatteryMillivolts3Length = 4;
    private static final int TrackerBatteryMilliamps3Offset = 44;
    private static final int TrackerBatteryMilliamps3Length = 4;

    private static final int TrackerGPSLatitudeOffset = 48;
    private static final int TrackerGPSLatitudeLength = 4;
    private static final int TrackerGPSLongitudeOffset = 52;
    private static final int TrackerGPSLongitudeLength = 4;
    private static final int TrackerGPSAltitudeOffset = 56;
    private static final int TrackerGPSAltitudeLength = 4;
    private static final int TrackerGPSSpeedOffset = 60;
    private static final int TrackerGPSSpeedLength = 4;
    private static final int TrackerGPSCourseOffset = 64;
    private static final int TrackerGPSCourseLength = 4;
    private static final int TrackerGPSYearOffset = 68;
    private static final int TrackerGPSYearLength = 2;
    private static final int TrackerGPSMonthOffset = 70;
    private static final int TrackerGPSMonthLength = 1;
    private static final int TrackerGPSDayOffset = 71;
    private static final int TrackerGPSDayLength = 1;
    private static final int TrackerGPSHoursOffset = 72;
    private static final int TrackerGPSHoursLength = 1;
    private static final int TrackerGPSMinutesOffset = 73;
    private static final int TrackerGPSMinutesLength = 1;
    private static final int TrackerGPSSecondsOffset = 74;
    private static final int TrackerGPSSecondsLength = 1;
    // 1 byte padding to force alignment to 4 byte boundary
    private static final int TrackerGPSFixQualityOffset = 76;
    private static final int TrackerGPSFixQualityLength = 1;
    private static final int TrackerGPSSVsUsedOffset = 77;
    private static final int TrackerGPSSVsUsedLength = 1;
    // 2 bytes padding to force alignment to 4 byte boundary
    private static final int TrackerGPSHDOPOffset = 80;
    private static final int TrackerGPSHDOPLength = 4;
    private static final int TrackerBMP180PressureOffset = 84;
    private static final int TrackerBMP180PressureLength = 4;
    private static final int TrackerBMP180TemperatureOffset = 88;
    private static final int TrackerBMP180TemperatureLength = 4;
    private static final int TrackerThermistorTemperatureOffset = 92;
    private static final int TrackerThermistorTemperatureLength = 4;
    private static final int TrackerCRC32Offset = 96;
    private static final int TrackerCRC32Length = 4;

    private static final short TrackerPayloadSize = 100;

    // Parsing engine member variables
    // constants
    private enum InternalState
    {
        WAITING_FOR_FIRST_HEADER_CHAR,
        WAITING_FOR_SECOND_HEADER_CHAR,
        WAITING_FOR_FIRST_TRAILER_CHAR,
        WAITING_FOR_SECOND_TRAILER_CHAR
    };

    private static final int PacketBufferSize = 1024;
    private final byte FirstHeaderChar = -1;
    private final byte SecondHeaderChar = -2;
    private final byte FirstTrailerChar = -3;
    private final byte SecondTrailerChar = -4;

    // data member variables
    private byte[] PacketBuffer;
    private int PacketBufferIndex;
    private InternalState ParserState;
    private int PacketsParsed;
    private int UniquePacketsParsed;
    private long LastMillisecondCount;

    // constructor
    TIDPacketParser()
    {
        PacketBuffer = new byte[PacketBufferSize];
        ResetParser();
        PacketsParsed = 0;
        UniquePacketsParsed = 0;
        LastMillisecondCount = 0;
    }

    // Getter functions
    // getter function to retrieve total packets parsed count
    public int GetPacketsParsed()
    {
        return PacketsParsed;
    }
    public int GetUniquePacketsParsed() { return UniquePacketsParsed; }

    public boolean AddBytesToParser(final byte[] bytesToAdd, final boolean AlertOnOnlyUniquePackets, TIDPacketData PacketData)
    {
        boolean haveCompletePacket = false;

        if(null != PacketData)
        {
            for (int i = 0; i < bytesToAdd.length; i++) {
                switch (ParserState) {
                    case WAITING_FOR_FIRST_HEADER_CHAR:
                        if (FirstHeaderChar == bytesToAdd[i]) {
                            PacketBuffer[PacketBufferIndex] = bytesToAdd[i];
                            PacketBufferIndex++;
                            ParserState = InternalState.WAITING_FOR_SECOND_HEADER_CHAR;
                        }
                        break;

                    case WAITING_FOR_SECOND_HEADER_CHAR:
                        if (SecondHeaderChar == bytesToAdd[i]) {
                            PacketBuffer[PacketBufferIndex] = bytesToAdd[i];
                            PacketBufferIndex++;
                            ParserState = InternalState.WAITING_FOR_FIRST_TRAILER_CHAR;
                        } else {
                            ResetParser();
                        }

                        break;

                    case WAITING_FOR_FIRST_TRAILER_CHAR:
                        PacketBuffer[PacketBufferIndex] = bytesToAdd[i];
                        PacketBufferIndex++;

                        if (FirstTrailerChar == bytesToAdd[i]) {
                            ParserState = InternalState.WAITING_FOR_SECOND_TRAILER_CHAR;
                        }

                        break;

                    case WAITING_FOR_SECOND_TRAILER_CHAR:
                        PacketBuffer[PacketBufferIndex] = bytesToAdd[i];
                        PacketBufferIndex++;

                        if (SecondTrailerChar == bytesToAdd[i]) {
                            haveCompletePacket = ParsePacket(AlertOnOnlyUniquePackets, PacketData);
                            PacketsParsed++;
                            ResetParser();
                        } else {
                            ParserState = InternalState.WAITING_FOR_FIRST_TRAILER_CHAR;
                        }

                        break;

                    default:
                        ResetParser();
                        break;
                }

                if (PacketBufferIndex >= PacketBufferSize) {
                    ResetParser();
                }
            }
        }

        return haveCompletePacket;
    }

    private void ResetParser()
    {
        ParserState = InternalState.WAITING_FOR_FIRST_HEADER_CHAR;
        PacketBufferIndex = 0;
    }

    private boolean ParsePacket(final boolean AlertOnOnlyUniquePackets, TIDPacketData PacketData)
    {
        boolean returnValue = false;

        // create a ByteBuffer wrapper for our packet buffer
        ByteBuffer buffer = ByteBuffer.wrap(PacketBuffer);
        // set byte ordering to little endian
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        // get, check TID payload size
        short TIDPayloadSize = buffer.getShort(TIDPayloadSizeOffset);
        // check size against what it should be
        if(TrackerPayloadSize == TIDPayloadSize)
        {
            long TIDPayloadCRC = C_uint32_t_to_Java_long(TIDPayloadCRCOffset);
            CRC32 crc = new CRC32();
            crc.reset();
            crc.update(PacketBuffer, TIDRSSIindBmOffset, (TIDPayloadCRCOffset - TIDRSSIindBmOffset));
            long TIDCRCValue = crc.getValue();

            // next, check tracker payload CRC
            // this has to be done with our own custom function, as we are reading an
            // unsigned 32 bit value into a Java long int (signed 64 bits)
            long TrackerCRC32 = C_uint32_t_to_Java_long(TIDPayloadOffset + TrackerCRC32Offset);
            crc.reset();
            crc.update(PacketBuffer, TIDPayloadOffset, (TIDPayloadLength - TrackerCRC32Length));
            long TrackerCRCValue = crc.getValue();

            // if CRC values match, we have a valid packet - continue
            if((TIDCRCValue == TIDPayloadCRC) && (TrackerCRCValue == TrackerCRC32))
            {
                // checks pass - we can parse
                // get RSSI (dBm)
                float TIDRSSIindBm = buffer.getFloat(TIDRSSIindBmOffset);
                PacketData.SetTIDRSSIindBm(TIDRSSIindBm);
                // now, get payload data
                // get tracker ID
                // this will be tricky as well, because it is a C-style string
                // which we need to slice out
                int from = TIDPayloadOffset + TrackerIDOffset;
                int to = from + TrackerIDLength;
                byte[] tempBytes = Arrays.copyOfRange(PacketBuffer, from, to);
                String TrackerID = new String(tempBytes).trim();
                PacketData.SetTrackerID(TrackerID);

                short TrackerRadioTemperature = buffer.getShort(TIDPayloadOffset + TrackerRadioTemperatureOffset);
                PacketData.SetTrackerRadioTemperature(TrackerRadioTemperature);

                // we use millisecond count to identify unique tracker packets (each tracker packet transmitted more than once)
                long MillisecondCount =  C_uint32_t_to_Java_long(TIDPayloadOffset + TrackerMillisecondCountOffset);
                boolean uniquePacket = (MillisecondCount != LastMillisecondCount);
                LastMillisecondCount = MillisecondCount;

                // if this is a unique packet, update our count
                if(uniquePacket)
                {
                    UniquePacketsParsed++;
                }

                // if we are only supposed to alert on unique packets, check to see if packet is unique before returning
                // true (meaning we have new data to show)
                if(AlertOnOnlyUniquePackets)
                {
                    if(uniquePacket)
                    {
                        returnValue = true;
                    }
                }
                else
                {
                    returnValue = true;
                }

                // copy millisecond count from tracker data
                PacketData.SetTrackerMillisecondCount(MillisecondCount);

                // get the current date/time, and set the date/time received (as milliseconds from epoch)
                Calendar dateTimeReceived = Calendar.getInstance();
                PacketData.SetDateTimeReceived(dateTimeReceived.getTimeInMillis());

                int TrackerBatteryMillivolts0 = buffer.getInt(TIDPayloadOffset + TrackerBatteryMillivolts0Offset);
                PacketData.SetTrackerBatteryMillivolts0(TrackerBatteryMillivolts0);

                int TrackerBatteryMilliamps0 = buffer.getInt(TIDPayloadOffset + TrackerBatteryMilliamps0Offset);
                PacketData.SetTrackerBatteryMilliamps0(TrackerBatteryMilliamps0);

                int TrackerBatteryMillivolts1 = buffer.getInt(TIDPayloadOffset + TrackerBatteryMillivolts1Offset);
                PacketData.SetTrackerBatteryMillivolts1(TrackerBatteryMillivolts1);

                int TrackerBatteryMilliamps1 = buffer.getInt(TIDPayloadOffset + TrackerBatteryMilliamps1Offset);
                PacketData.SetTrackerBatteryMilliamps1(TrackerBatteryMilliamps1);

                int TrackerBatteryMillivolts2 = buffer.getInt(TIDPayloadOffset + TrackerBatteryMillivolts2Offset);
                PacketData.SetTrackerBatteryMillivolts2(TrackerBatteryMillivolts2);

                int TrackerBatteryMilliamps2 = buffer.getInt(TIDPayloadOffset + TrackerBatteryMilliamps2Offset);
                PacketData.SetTrackerBatteryMilliamps2(TrackerBatteryMilliamps2);

                int TrackerBatteryMillivolts3 = buffer.getInt(TIDPayloadOffset + TrackerBatteryMillivolts3Offset);
                PacketData.SetTrackerBatteryMillivolts3(TrackerBatteryMillivolts3);

                int TrackerBatteryMilliamps3 = buffer.getInt(TIDPayloadOffset + TrackerBatteryMilliamps3Offset);
                PacketData.SetTrackerBatteryMilliamps3(TrackerBatteryMilliamps3);

                float TrackerGPSLatitude = buffer.getFloat(TIDPayloadOffset + TrackerGPSLatitudeOffset);
                PacketData.SetTrackerGPSLatitude(TrackerGPSLatitude);

                float TrackerGPSLongitude = buffer.getFloat(TIDPayloadOffset + TrackerGPSLongitudeOffset);
                PacketData.SetTrackerGPSLongitude(TrackerGPSLongitude);

                float TrackerGPSAltitude = buffer.getFloat(TIDPayloadOffset + TrackerGPSAltitudeOffset);
                PacketData.SetTrackerGPSAltitude(TrackerGPSAltitude);

                float TrackerGPSSpeed = buffer.getFloat(TIDPayloadOffset + TrackerGPSSpeedOffset);
                PacketData.SetTrackerGPSSpeed(TrackerGPSSpeed);

                float TrackerGPSCourse = buffer.getFloat(TIDPayloadOffset + TrackerGPSCourseOffset);
                PacketData.SetTrackerGPSCourse(TrackerGPSCourse);

                short TrackerGPSYear = buffer.getShort(TIDPayloadOffset + TrackerGPSYearOffset);
                PacketData.SetTrackerGPSYear(TrackerGPSYear);

                byte TrackerGPSMonth = PacketBuffer[TIDPayloadOffset + TrackerGPSMonthOffset];
                PacketData.SetTrackerGPSMonth(TrackerGPSMonth);

                byte TrackerGPSDay = PacketBuffer[TIDPayloadOffset + TrackerGPSDayOffset];
                PacketData.SetTrackerGPSDay(TrackerGPSDay);

                byte TrackerGPSHours = PacketBuffer[TIDPayloadOffset + TrackerGPSHoursOffset];
                PacketData.SetTrackerGPSHours(TrackerGPSHours);

                byte TrackerGPSMinutes = PacketBuffer[TIDPayloadOffset + TrackerGPSMinutesOffset];
                PacketData.SetTrackerGPSMinutes(TrackerGPSMinutes);

                byte TrackerGPSSeconds = PacketBuffer[TIDPayloadOffset + TrackerGPSSecondsOffset];
                PacketData.SetTrackerGPSSeconds(TrackerGPSSeconds);

                byte TrackerGPSFixQuality = PacketBuffer[TIDPayloadOffset + TrackerGPSFixQualityOffset];
                PacketData.SetTrackerGPSFixQuality(TrackerGPSFixQuality);

                byte TrackerGPSSVsUsed = PacketBuffer[TIDPayloadOffset + TrackerGPSSVsUsedOffset];
                PacketData.SetTrackerGPSSVsUsed(TrackerGPSSVsUsed);

                float TrackerGPSHDOP = buffer.getFloat(TIDPayloadOffset + TrackerGPSHDOPOffset);
                PacketData.SetTrackerGPSHDOP(TrackerGPSHDOP);

                int TrackerBMP180Pressure = buffer.getInt(TIDPayloadOffset + TrackerBMP180PressureOffset);
                PacketData.SetTrackerBMP180Pressure(TrackerBMP180Pressure);

                float TrackerBMP180Temperature = buffer.getFloat(TIDPayloadOffset + TrackerBMP180TemperatureOffset);
                PacketData.SetTrackerBMP180Temperature(TrackerBMP180Temperature);

                float TrackerThermistorTemperature = buffer.getFloat(TIDPayloadOffset + TrackerThermistorTemperatureOffset);
                PacketData.SetTrackerThermistorTemperature(TrackerThermistorTemperature);
            }
        }

        return returnValue;
    }

    private long C_uint32_t_to_Java_long(int PacketBufferOffset)
    {
        long returnValue;

        returnValue = 0xff & PacketBuffer[PacketBufferOffset + 3];
        returnValue <<= 8;
        returnValue += 0xff & PacketBuffer[PacketBufferOffset + 2];
        returnValue <<= 8;
        returnValue += 0xff & PacketBuffer[PacketBufferOffset + 1];
        returnValue <<= 8;
        returnValue += 0xff & PacketBuffer[PacketBufferOffset];

        return returnValue;
    }
}


