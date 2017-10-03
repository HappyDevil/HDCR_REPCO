package com.repandco.repco.constants;

public class Values {

    public static class TYPES {
        public static final int PROFESSIONAL_TYPE = 1;
        public static final int ENTERPRISE_TYPE = 2;
        public static final int DEFAULT_TYPE = 0;
    }

    public static class POSTS {
        public static final long STANDARD_POST = 0;
        public static final long CDD_JOB_POST = 1;
        public static final long CDI_JOB_POST = 2;
        public static final long EXTRA_JOB_POST = 3;
    }


    public final static class GENDERS {
        public static final int MALE = 1;
        public static final int FEMALE = 2;
    }

    public static class Visible
    {
        public static final int PUBLIC = 1;
        public static final int PRIVATE = 2;

    }

    public static class NEWS{
        public static final int FOLLOW = 0;
        public static final int DISFOLLOW = 2;
        public static final int FRIEND  =1;
        public static final int WORK = 3;
        public static final int LIKE = 4;
    }

    public static class SIZES{
        public static final int TAG_SIZES = 5;
    }


    public static class URLS {

        public static final String STANDARD = "standard";
    }

    public static class REQUEST{
        public static final int LOAD_POST_PHOTO = 10001;
    }
}
