import java.util.*;

public class Base
{

    static String hl7message = "MSH|^~&|DCC|SVH|SVH||20140421085940||ADT^A31|01708235|P|2.2|||AL|AL|US" + "\nEVN|A08|20140421085940||01|super|\n" +
        "PID|1||A123456||DEAN^JIMMY^A^^||19120101|M||1|1 NORTH SUMMIT A 1^^KENT^WA^98031^^^KING|KING|(555)555-5511|||U||433106|123456789|||||||||";

    public static void main(String[] args)
    {
        parseHL7(hl7message);
    }

    /**
     * Parse the message and print contents according to the specification.
     */
    static void parseHL7(String hl7)
    {
        if (hl7 == null || hl7.trim().isEmpty())
        {
            return;
        }

        Map<String, Segment> map = parse(hl7);
        print(map);
    }

    static Map<String, Segment> parse(String hl7)
    {
        Map<String, Segment> map = new HashMap<>();

        String[] segments = hl7.split("[\\r\\n]");

        for (String s : segments)
        {
            Segment seg = new Segment(s);
            map.put(seg.getSegName(), seg);
        }

        return map;
    }

    static void print(Map<String, Segment> map)
    {
        Segment s = map.get("PID");

        if (s != null)
        {
            printIfPresent(noNull(s.text(7, null)));
            printIfPresent(noNull(s.text(11, 1)));
            printIfPresent(noNull(s.text(11, 3)));
            printIfPresent(noNull(s.text(11, 4)));
            printIfPresent(noNull(s.text(11, 5)));
            printIfPresent(noNull(s.text(5, 1)));
            printIfPresent(noNull(s.text(5, 2)));
            printIfPresent(noNull(s.text(5, 3)));
            printIfPresent(noNull(s.text(3, null)));
        }

        s = map.get("EVN");
        if (s != null)
        {
            printIfPresent(noNull(s.text(2, null)));
        }

        s = map.get("MSH");
        if (s != null)
        {
            printIfPresent(noNull(s.text(8, 1))+"^"+noNull(s.text(8, 2)));
        }
    }

    static String noNull(String s)
    {
        if (s == null)
        {
            return "";
        }
        return s;
    }

    static void printIfPresent(String s)
    {
        if (s != null && !s.isEmpty())
        {
            System.out.println(s);
        }
    }

    static class Segment
    {
        String segName;
        String segText;
        Map<Integer, Map<Integer, String>> fieldsMap;

        public Segment(String segmentText)
        {
            fieldsMap = new HashMap<>();
            this.segText = segmentText;
            extract(segmentText);
        }

        public String getSegName()
        {
            return segName;
        }

        public void setSegName(String segName)
        {
            this.segName = segName;
        }

        public String getSegText()
        {
            return segText;
        }

        public void setSegText(String segText)
        {
            this.segText = segText;
        }

        public Map<Integer, Map<Integer, String>> getFieldsMap()
        {
            return fieldsMap;
        }

        public void setFieldsMap(Map<Integer, Map<Integer, String>> fieldsMap)
        {
            this.fieldsMap = fieldsMap;
        }

        private void extract(String text)
        {
            //Invalid if smaller than 3 characters
            assert text.length() > 2;

            this.segName = text.substring(0, 3);
            String rem = text.substring(3, text.length() - 1);
            String[] fields = rem.split("\\|");

            for (int c = 0; c < fields.length; c++)
            {
                String[] subfields = fields[c].split("\\^");

                Map<Integer, String> fMap = fieldsMap.get(c);

                if (fMap == null)
                {
                    fMap = new HashMap<>();
                    fieldsMap.put(c, fMap);
                }

                for (int d = 0; d < subfields.length; d++)
                {
                    fMap.put(d, subfields[d]);
                }
            }
        }

        public String text(Integer primary, Integer secondary)
        {
            if (primary==null || primary<1)
            {
                primary = 0;
            }

            Map<Integer, String> fMap = fieldsMap.get(primary);

            if (fMap==null)
            {
                return "";
            }

            if (secondary == null)
            {
                return fieldsToString(fMap);
            }

            secondary--;

            return fMap.get(secondary);
        }

        private String fieldsToString(Map<Integer, String> map)
        {
            StringBuilder sb = new StringBuilder();

            for (int c = 0; c < map.size(); c++)
            {
                String s = map.get(c);
                if (s != null)
                {
                    sb.append(s);
                }
            }

            return sb.toString();
        }
    }

}


