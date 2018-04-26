package edu.umich.mkirkhar.PicoballoonTracker;

import android.os.Bundle;

class TIDPacketData
{
    private final float MAX_GPS_HDOP_FOR_GOOD_GPS = 10.0f;

    // private member variables parsed from TID packet
    private float TIDRSSIindBm;
    // private member variables parsed from Picoballoon tracker data payload
    private String TrackerID;
    private short TrackerRadioTemperature;
    private long TrackerMillisecondCount;
    private int TrackerBatteryMillivolts0;
    private int TrackerBatteryMilliamps0;
    private int TrackerBatteryMillivolts1;
    private int TrackerBatteryMilliamps1;
    private int TrackerBatteryMillivolts2;
    private int TrackerBatteryMilliamps2;
    private int TrackerBatteryMillivolts3;
    private int TrackerBatteryMilliamps3;
    private float TrackerGPSLatitude;
    private float TrackerGPSLongitude;
    private float TrackerGPSAltitude;
    private float TrackerGPSSpeed;
    private float TrackerGPSCourse;
    private short TrackerGPSYear;
    private byte TrackerGPSMonth;
    private byte TrackerGPSDay;
    private byte TrackerGPSHours;
    private byte TrackerGPSMinutes;
    private byte TrackerGPSSeconds;
    private byte TrackerGPSFixQuality;
    private byte TrackerGPSSVsUsed;
    private float TrackerGPSHDOP;
    private int TrackerBMP180Pressure;
    private float TrackerBMP180Temperature;
    private float TrackerThermistorTemperature;

    // date/time of receipt of packet on device
    private long DateTimeReceived;

    public TIDPacketData()
    {
        TrackerID = "";
        TrackerGPSHDOP = 99.9f;
    }

    public float GetTIDRSSIindBm()
    {
        return TIDRSSIindBm;
    }
    public void SetTIDRSSIindBm(final float newValue) { TIDRSSIindBm = newValue; }

    public String GetTrackerID()
    {
        return TrackerID;
    }
    public void SetTrackerID(final String newValue) { TrackerID = newValue; }

    public short GetTrackerRadioTemperature()
    {
        return TrackerRadioTemperature;
    }
    public void SetTrackerRadioTemperature(final short newValue) { TrackerRadioTemperature = newValue; }

    public long GetTrackerMillisecondCount()
    {
        return TrackerMillisecondCount;
    }
    public void SetTrackerMillisecondCount(final long newValue) { TrackerMillisecondCount = newValue; }

    public int GetTrackerBatteryMillivolts0()
    {
        return TrackerBatteryMillivolts0;
    }
    public void SetTrackerBatteryMillivolts0(final int newValue) { TrackerBatteryMillivolts0 = newValue; }

    public int GetTrackerBatteryMilliamps0()
    {
        return TrackerBatteryMilliamps0;
    }
    public void SetTrackerBatteryMilliamps0(final int newValue) { TrackerBatteryMilliamps0 = newValue; }

    public int GetTrackerBatteryMillivolts1()
    {
        return TrackerBatteryMillivolts1;
    }
    public void SetTrackerBatteryMillivolts1(final int newValue) { TrackerBatteryMillivolts1 = newValue; }

    public int GetTrackerBatteryMilliamps1()
    {
        return TrackerBatteryMilliamps1;
    }
    public void SetTrackerBatteryMilliamps1(final int newValue) { TrackerBatteryMilliamps1 = newValue; }

    public int GetTrackerBatteryMillivolts2()
    {
        return TrackerBatteryMillivolts2;
    }
    public void SetTrackerBatteryMillivolts2(final int newValue) { TrackerBatteryMillivolts2 = newValue; }

    public int GetTrackerBatteryMilliamps2()
    {
        return TrackerBatteryMilliamps2;
    }
    public void SetTrackerBatteryMilliamps2(final int newValue) { TrackerBatteryMilliamps2 = newValue; }

    public int GetTrackerBatteryMillivolts3()
    {
        return TrackerBatteryMillivolts3;
    }
    public void SetTrackerBatteryMillivolts3(final int newValue) { TrackerBatteryMillivolts3 = newValue; }

    public int TrackerBatteryMilliamps3()
    {
        return TrackerBatteryMilliamps3;
    }
    public void SetTrackerBatteryMilliamps3(final int newValue) { TrackerBatteryMilliamps3 = newValue; }

    public float GetTrackerGPSLatitude()
    {
        return TrackerGPSLatitude;
    }
    public void SetTrackerGPSLatitude(final float newValue) { TrackerGPSLatitude = newValue; }

    public float GetTrackerGPSLongitude()
    {
        return TrackerGPSLongitude;
    }
    public void SetTrackerGPSLongitude(final float newValue) { TrackerGPSLongitude = newValue; }

    public float GetTrackerGPSAltitude()
    {
        return TrackerGPSAltitude;
    }
    public void SetTrackerGPSAltitude(final float newValue) { TrackerGPSAltitude = newValue; }

    public float GetTrackerGPSSpeed()
    {
        return TrackerGPSSpeed;
    }
    public void SetTrackerGPSSpeed(final float newValue) { TrackerGPSSpeed = newValue; }

    public float GetTrackerGPSCourse()
    {
        return TrackerGPSCourse;
    }
    public void SetTrackerGPSCourse(final float newValue) { TrackerGPSCourse = newValue; }

    public short GetTrackerGPSYear()
    {
        return TrackerGPSYear;
    }
    public void SetTrackerGPSYear(final short newValue) { TrackerGPSYear = newValue; }

    public byte GetTrackerGPSMonth()
    {
        return TrackerGPSMonth;
    }
    public void SetTrackerGPSMonth(final byte newValue) { TrackerGPSMonth = newValue; }

    public byte GetTrackerGPSDay()
    {
        return TrackerGPSDay;
    }
    public void SetTrackerGPSDay(final byte newValue) { TrackerGPSDay = newValue; }

    public byte GetTrackerGPSHours()
    {
        return TrackerGPSHours;
    }
    public void SetTrackerGPSHours(final byte newValue) { TrackerGPSHours = newValue; }

    public byte GetTrackerGPSMinutes()
    {
        return TrackerGPSMinutes;
    }
    public void SetTrackerGPSMinutes(final byte newValue) { TrackerGPSMinutes = newValue; }

    public byte GetTrackerGPSSeconds()
    {
        return TrackerGPSSeconds;
    }
    public void SetTrackerGPSSeconds(final byte newValue) { TrackerGPSSeconds = newValue; }

    public byte GetTrackerGPSFixQuality()
    {
        return TrackerGPSFixQuality;
    }
    public void SetTrackerGPSFixQuality(final byte newValue) { TrackerGPSFixQuality = newValue; }

    public byte GetTrackerGPSSVsUsed()
    {
        return TrackerGPSSVsUsed;
    }
    public void SetTrackerGPSSVsUsed(final byte newValue) { TrackerGPSSVsUsed = newValue; }

    public float GetTrackerGPSHDOP()
    {
        return TrackerGPSHDOP;
    }
    public void SetTrackerGPSHDOP(final float newValue) { TrackerGPSHDOP = newValue; }

    public int GetTrackerBMP180Pressure()
    {
        return TrackerBMP180Pressure;
    }
    public void SetTrackerBMP180Pressure(final int newValue) { TrackerBMP180Pressure = newValue; }

    public float GetTrackerBMP180Temperature()
    {
        return TrackerBMP180Temperature;
    }
    public void SetTrackerBMP180Temperature(final float newValue) { TrackerBMP180Temperature = newValue; }

    public float GetTrackerThermistorTemperature()
    {
        return TrackerThermistorTemperature;
    }
    public void SetTrackerThermistorTemperature(final float newValue) { TrackerThermistorTemperature = newValue; }

    public long GetDateTimeReceived() { return DateTimeReceived; }
    public void SetDateTimeReceived(final long newValue) { DateTimeReceived = newValue; }

    public boolean GPSIsGood()
    {
        boolean returnValue = false;

        if((0.0 != TrackerGPSLatitude) && (0.0 != TrackerGPSLongitude) && (TrackerGPSHDOP < MAX_GPS_HDOP_FOR_GOOD_GPS))
        {
            returnValue = true;
        }

        return returnValue;
    }

    // name keys for TID packet data contained in a Bundle
    private static final String TIDRSSIindBmFieldName = "TIDRSSIindBm";
    // private member variables parsed from Picoballoon tracker data payload
    private static final String TrackerIDFieldName = "TrackerID";
    private static final String TrackerRadioTemperatureFieldName = "TrackerRadioTemperature";
    private static final String TrackerMillisecondCountFieldName = "TrackerMillisecondCount";
    private static final String TrackerBatteryMillivolts0FieldName = "TrackerBatteryMillivolts0";
    private static final String TrackerBatteryMilliamps0FieldName = "TrackerBatteryMilliamps0";
    private static final String TrackerBatteryMillivolts1FieldName = "TrackerBatteryMillivolts1";
    private static final String TrackerBatteryMilliamps1FieldName = "TrackerBatteryMilliamps1";
    private static final String TrackerBatteryMillivolts2FieldName = "TrackerBatteryMillivolts2";
    private static final String TrackerBatteryMilliamps2FieldName = "TrackerBatteryMilliamps2";
    private static final String TrackerBatteryMillivolts3FieldName = "TrackerBatteryMillivolts3";
    private static final String TrackerBatteryMilliamps3FieldName = "TrackerBatteryMilliamps3";
    private static final String TrackerGPSLatitudeFieldName = "TrackerGPSLatitude";
    private static final String TrackerGPSLongitudeFieldName = "TrackerGPSLongitude";
    private static final String TrackerGPSAltitudeFieldName = "TrackerGPSAltitude";
    private static final String TrackerGPSSpeedFieldName = "TrackerGPSSpeed";
    private static final String TrackerGPSCourseFieldName = "TrackerGPSCourse";
    private static final String TrackerGPSYearFieldName = "TrackerGPSYear";
    private static final String TrackerGPSMonthFieldName = "TrackerGPSMonth";
    private static final String TrackerGPSDayFieldName = "TrackerGPSDay";
    private static final String TrackerGPSHoursFieldName = "TrackerGPSHours";
    private static final String TrackerGPSMinutesFieldName = "TrackerGPSMinutes";
    private static final String TrackerGPSSecondsFieldName = "TrackerGPSSeconds";
    private static final String TrackerGPSFixQualityFieldName = "TrackerGPSFixQuality";
    private static final String TrackerGPSSVsUsedFieldName = "TrackerGPSSVsUsed";
    private static final String TrackerGPSHDOPFieldName = "TrackerGPSHDOP";
    private static final String TrackerBMP180PressureFieldName = "TrackerBMP180Pressure";
    private static final String TrackerBMP180TemperatureFieldName = "TrackerBMP180Temperature";
    private static final String TrackerThermistorTemperatureFieldName = "TrackerThermistorTemperature";
    // name key for date/time of receipt of packet on device
    private static final String DateTimeReceivedFieldName = "DateTimeReceived";

    public Bundle CreateTIDDataBundle()
    {
        Bundle bundle = new Bundle();

        PutTIDDataPacketIntoBundle(bundle);

        return bundle;
    }

    public void PutTIDDataPacketIntoBundle(Bundle bundle)
    {
        if(null != bundle)
        {
            bundle.putFloat(TIDRSSIindBmFieldName, TIDRSSIindBm);
            bundle.putString(TrackerIDFieldName, TrackerID);
            bundle.putShort(TrackerRadioTemperatureFieldName, TrackerRadioTemperature);
            bundle.putLong(TrackerMillisecondCountFieldName, TrackerMillisecondCount);

            bundle.putInt(TrackerBatteryMillivolts0FieldName, TrackerBatteryMillivolts0);
            bundle.putInt(TrackerBatteryMilliamps0FieldName, TrackerBatteryMilliamps0);

            bundle.putInt(TrackerBatteryMillivolts1FieldName, TrackerBatteryMillivolts1);
            bundle.putInt(TrackerBatteryMilliamps1FieldName, TrackerBatteryMilliamps1);

            bundle.putInt(TrackerBatteryMillivolts2FieldName, TrackerBatteryMillivolts2);
            bundle.putInt(TrackerBatteryMilliamps2FieldName, TrackerBatteryMilliamps2);

            bundle.putInt(TrackerBatteryMillivolts3FieldName, TrackerBatteryMillivolts3);
            bundle.putInt(TrackerBatteryMilliamps3FieldName, TrackerBatteryMilliamps3);

            bundle.putFloat(TrackerGPSLatitudeFieldName, TrackerGPSLatitude);
            bundle.putFloat(TrackerGPSLongitudeFieldName, TrackerGPSLongitude);
            bundle.putFloat(TrackerGPSAltitudeFieldName, TrackerGPSAltitude);
            bundle.putFloat(TrackerGPSSpeedFieldName, TrackerGPSSpeed);
            bundle.putFloat(TrackerGPSCourseFieldName, TrackerGPSCourse);

            bundle.putShort(TrackerGPSYearFieldName, TrackerGPSYear);
            bundle.putByte(TrackerGPSMonthFieldName, TrackerGPSMonth);
            bundle.putByte(TrackerGPSDayFieldName, TrackerGPSDay);
            bundle.putByte(TrackerGPSHoursFieldName, TrackerGPSHours);
            bundle.putByte(TrackerGPSMinutesFieldName, TrackerGPSMinutes);
            bundle.putByte(TrackerGPSSecondsFieldName, TrackerGPSSeconds);

            bundle.putByte(TrackerGPSFixQualityFieldName, TrackerGPSFixQuality);
            bundle.putByte(TrackerGPSSVsUsedFieldName, TrackerGPSSVsUsed);
            bundle.putFloat(TrackerGPSHDOPFieldName, TrackerGPSHDOP);

            bundle.putInt(TrackerBMP180PressureFieldName, TrackerBMP180Pressure);
            bundle.putFloat(TrackerBMP180TemperatureFieldName, TrackerBMP180Temperature);
            bundle.putFloat(TrackerThermistorTemperatureFieldName, TrackerThermistorTemperature);

            bundle.putLong(DateTimeReceivedFieldName, DateTimeReceived);
        }
    }

    static public float GetTIDRSSIindBmFromBundle(Bundle bundle)
    {
        float returnValue = 0.0f;

        if(null != bundle)
        {
            returnValue = bundle.getFloat(TIDRSSIindBmFieldName);
        }

        return returnValue;
    }

    static public String GetTrackerIDFromBundle(Bundle bundle)
    {
        String returnValue = new String();

        if(null != bundle)
        {
            returnValue = bundle.getString(TrackerIDFieldName);
        }

        return returnValue;
    }

    static public short GetTrackerRadioTemperatureFromBundle(Bundle bundle)
    {
        short returnValue = 0;

        if(null != bundle)
        {
            returnValue = bundle.getShort(TrackerRadioTemperatureFieldName);
        }

        return returnValue;
    }

    static public long GetTrackerMillisecondCountFromBundle(Bundle bundle)
    {
        long returnValue = 0;

        if(null != bundle)
        {
            returnValue = bundle.getLong(TrackerMillisecondCountFieldName);
        }

        return returnValue;
    }

    static public int GetTrackerBatteryMillivolts0FromBundle(Bundle bundle)
    {
        int returnValue = 0;

        if(null != bundle)
        {
            returnValue = bundle.getInt(TrackerBatteryMillivolts0FieldName);
        }

        return returnValue;
    }

    static public int GetTrackerBatteryMilliamps0FromBundle(Bundle bundle)
    {
        int returnValue = 0;

        if(null != bundle)
        {
            returnValue = bundle.getInt(TrackerBatteryMilliamps0FieldName);
        }

        return returnValue;
    }

    static public int GetTrackerBatteryMillivolts1FromBundle(Bundle bundle)
    {
        int returnValue = 0;

        if(null != bundle)
        {
            returnValue = bundle.getInt(TrackerBatteryMillivolts1FieldName);
        }

        return returnValue;
    }

    static public int GetTrackerBatteryMilliamps1FromBundle(Bundle bundle)
    {
        int returnValue = 0;

        if(null != bundle)
        {
            returnValue = bundle.getInt(TrackerBatteryMilliamps1FieldName);
        }

        return returnValue;
    }

    static public int GetTrackerBatteryMillivolts2FromBundle(Bundle bundle)
    {
        int returnValue = 0;

        if(null != bundle)
        {
            returnValue = bundle.getInt(TrackerBatteryMillivolts2FieldName);
        }

        return returnValue;
    }

    static public int GetTrackerBatteryMilliamps2FromBundle(Bundle bundle)
    {
        int returnValue = 0;

        if(null != bundle)
        {
            returnValue = bundle.getInt(TrackerBatteryMilliamps2FieldName);
        }

        return returnValue;
    }

    static public int GetTrackerBatteryMillivolts3FromBundle(Bundle bundle)
    {
        int returnValue = 0;

        if(null != bundle)
        {
            returnValue = bundle.getInt(TrackerBatteryMillivolts3FieldName);
        }

        return returnValue;
    }

    static public int GetTrackerBatteryMilliamps3FromBundle(Bundle bundle)
    {
        int returnValue = 0;

        if(null != bundle)
        {
            returnValue = bundle.getInt(TrackerBatteryMilliamps3FieldName);
        }

        return returnValue;
    }

    static public float GetTrackerGPSLatitudeFromBundle(Bundle bundle)
    {
        float returnValue = 0.0f;

        if(null != bundle)
        {
            returnValue = bundle.getFloat(TrackerGPSLatitudeFieldName);
        }

        return returnValue;
    }

    static public float GetTrackerGPSLongitudeFromBundle(Bundle bundle)
    {
        float returnValue = 0.0f;

        if(null != bundle)
        {
            returnValue = bundle.getFloat(TrackerGPSLongitudeFieldName);
        }

        return returnValue;
    }

    static public float GetTrackerGPSAltitudeFromBundle(Bundle bundle)
    {
        float returnValue = 0.0f;

        if(null != bundle)
        {
            returnValue = bundle.getFloat(TrackerGPSAltitudeFieldName);
        }

        return returnValue;
    }

    static public float GetTrackerGPSSpeedFromBundle(Bundle bundle)
    {
        float returnValue = 0.0f;

        if(null != bundle)
        {
            returnValue = bundle.getFloat(TrackerGPSSpeedFieldName);
        }

        return returnValue;
    }

    static public float GetTrackerGPSCourseFromBundle(Bundle bundle)
    {
        float returnValue = 0.0f;

        if(null != bundle)
        {
            returnValue = bundle.getFloat(TrackerGPSCourseFieldName);
        }

        return returnValue;
    }

    static public short GetTrackerGPSYearFromBundle(Bundle bundle)
    {
        short returnValue = 0;

        if(null != bundle)
        {
            returnValue = bundle.getShort(TrackerGPSYearFieldName);
        }

        return returnValue;
    }

    static public byte GetTrackerGPSMonthFromBundle(Bundle bundle)
    {
        byte returnValue = 0;

        if(null != bundle)
        {
            returnValue = bundle.getByte(TrackerGPSMonthFieldName);
        }

        return returnValue;
    }

    static public byte GetTrackerGPSDayFromBundle(Bundle bundle)
    {
        byte returnValue = 0;

        if(null != bundle)
        {
            returnValue = bundle.getByte(TrackerGPSDayFieldName);
        }

        return returnValue;
    }

    static public byte GetTrackerGPSHoursFromBundle(Bundle bundle)
    {
        byte returnValue = 0;

        if(null != bundle)
        {
            returnValue = bundle.getByte(TrackerGPSHoursFieldName);
        }

        return returnValue;
    }

    static public byte GetTrackerGPSMinutesFromBundle(Bundle bundle)
    {
        byte returnValue = 0;

        if(null != bundle)
        {
            returnValue = bundle.getByte(TrackerGPSMinutesFieldName);
        }

        return returnValue;
    }

    static public byte GetTrackerGPSSecondsFromBundle(Bundle bundle)
    {
        byte returnValue = 0;

        if(null != bundle)
        {
            returnValue =  bundle.getByte(TrackerGPSSecondsFieldName);
        }

        return returnValue;
    }

    static public byte GetTrackerGPSFixQualityFromBundle(Bundle bundle)
    {
        byte returnValue = 0;

        if(null != bundle)
        {
            returnValue = bundle.getByte(TrackerGPSFixQualityFieldName);
        }

        return returnValue;
    }

    static public byte GetTrackerGPSSVsUsedFromBundle(Bundle bundle)
    {
        byte returnValue = 0;

        if(null != bundle)
        {
            returnValue = bundle.getByte(TrackerGPSSVsUsedFieldName);
        }

        return returnValue;
    }

    static public float GetTrackerGPSHDOPFromBundle(Bundle bundle)
    {
        float returnValue = 0.0f;

        if(null != bundle)
        {
            returnValue = bundle.getFloat(TrackerGPSHDOPFieldName);
        }

        return returnValue;
    }

    static public int GetTrackerBMP180PressureFromBundle(Bundle bundle)
    {
        int returnValue = 0;

        if(null != bundle)
        {
            returnValue = bundle.getInt(TrackerBMP180PressureFieldName);
        }

        return returnValue;
    }

    static public float GetTrackerBMP180TemperatureFromBundle(Bundle bundle)
    {
        float returnValue = 0.0f;

        if(null != bundle)
        {
            returnValue = bundle.getFloat(TrackerBMP180TemperatureFieldName);
        }

        return returnValue;
    }

    static public float GetTrackerThermistorTemperatureFromBundle(Bundle bundle)
    {
        float returnValue = 0.0f;

        if(null != bundle)
        {
            returnValue = bundle.getFloat(TrackerThermistorTemperatureFieldName);
        }

        return returnValue;
    }

    static public long GetDateTimeReceived(Bundle bundle)
    {
        long returnValue = 0;

        if (null != bundle)
        {
            returnValue = bundle.getLong(DateTimeReceivedFieldName);
        }

        return returnValue;
    }

    static public int GetTrackerTransmitBatteryMillivoltsFromBundle(Bundle bundle)
    {
        int returnValue = 0;

        if(null != bundle)
        {
            int batteryMillivoltsGPSOff = GetTrackerBatteryMillivolts2FromBundle(bundle);
            int batteryMillivoltsGPSOn = GetTrackerBatteryMillivolts3FromBundle(bundle);

            if(batteryMillivoltsGPSOn > 0)
            {
                returnValue = batteryMillivoltsGPSOn;
            }
            else
            {
                returnValue = batteryMillivoltsGPSOff;
            }
        }

        return returnValue;
    }

    static public int GetTrackerTransmitBatteryMilliampsFromBundle(Bundle bundle)
    {
        int returnValue = 0;

        if(null != bundle)
        {
            int batteryMilliampsGPSOff = GetTrackerBatteryMilliamps2FromBundle(bundle);
            int batteryMilliampsGPSOn = GetTrackerBatteryMilliamps3FromBundle(bundle);

            if(batteryMilliampsGPSOn > 0)
            {
                returnValue = batteryMilliampsGPSOn;
            }
            else
            {
                returnValue = batteryMilliampsGPSOff;
            }
        }

        return returnValue;
    }
}
