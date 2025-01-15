package io.isometrik.meeting.builder.meetings;

import java.util.List;

/**
 * The query builder for fetching meetings request.
 */
public class FetchMeetingsQuery {
    private final Integer sort;
    private final Integer limit;
    private final Integer skip;
    private final List<String> membersIncluded;
    private final List<String> membersExactly;
    private final List<String> meetingIds;
    private final String customType;
    private final Integer meetingType;
    private final Boolean includeMembers;
    private final Integer membersSkip;
    private final Integer membersLimit;
    private final String searchTag;
    private final Boolean hdMeeting;
    private final Boolean recorded;
    private final Boolean audioOnly;

    private final String userToken;

    private FetchMeetingsQuery(Builder builder) {
        this.sort = builder.sort;
        this.limit = builder.limit;
        this.skip = builder.skip;
        this.includeMembers = builder.includeMembers;
        this.membersIncluded = builder.membersIncluded;
        this.membersExactly = builder.membersExactly;
        this.meetingIds = builder.meetingIds;
        this.customType = builder.customType;
        this.meetingType = builder.meetingType;
        this.membersSkip = builder.membersSkip;
        this.membersLimit = builder.membersLimit;
        this.searchTag = builder.searchTag;
        this.hdMeeting = builder.hdMeeting;
        this.recorded = builder.recorded;
        this.audioOnly = builder.audioOnly;
        this.userToken = builder.userToken;
    }

    /**
     * The builder class for building fetch meetings query.
     */
    public static class Builder {
        private Integer sort;
        private Integer limit;
        private Integer skip;
        private Boolean includeMembers;
        private List<String> membersIncluded;
        private List<String> membersExactly;
        private String customType;
        private Integer membersSkip;
        private Integer membersLimit;
        private String searchTag;
        private List<String> meetingIds;
        private Integer meetingType;
        private Boolean hdMeeting;
        private Boolean recorded;
        private Boolean audioOnly;
        private String userToken;

        /**
         * Instantiates a new Builder.
         */
        public Builder() {
        }

        /**
         * Sets sort.
         *
         * @param sort the sort
         * @return the sort
         */
        public Builder setSort(Integer sort) {
            this.sort = sort;
            return this;
        }

        /**
         * Sets limit.
         *
         * @param limit the limit
         * @return the limit
         */
        public Builder setLimit(Integer limit) {
            this.limit = limit;
            return this;
        }

        /**
         * Sets skip.
         *
         * @param skip the skip
         * @return the skip
         */
        public Builder setSkip(Integer skip) {
            this.skip = skip;
            return this;
        }

        /**
         * Sets include members.
         *
         * @param includeMembers the include members
         * @return the include members
         */
        public Builder setIncludeMembers(Boolean includeMembers) {
            this.includeMembers = includeMembers;
            return this;
        }

        /**
         * Sets members included.
         *
         * @param membersIncluded the members included
         * @return the members included
         */
        public Builder setMembersIncluded(List<String> membersIncluded) {
            this.membersIncluded = membersIncluded;
            return this;
        }

        /**
         * Sets members exactly.
         *
         * @param membersExactly the members exactly
         * @return the members exactly
         */
        public Builder setMembersExactly(List<String> membersExactly) {
            this.membersExactly = membersExactly;
            return this;
        }


        /**
         * Sets custom type.
         *
         * @param customType the custom type
         * @return the custom type
         */
        public Builder setCustomType(String customType) {
            this.customType = customType;
            return this;
        }

        /**
         * Sets members skip.
         *
         * @param membersSkip the members skip
         * @return the members skip
         */
        public Builder setMembersSkip(Integer membersSkip) {
            this.membersSkip = membersSkip;
            return this;
        }

        /**
         * Sets members limit.
         *
         * @param membersLimit the members limit
         * @return the members limit
         */
        public Builder setMembersLimit(Integer membersLimit) {
            this.membersLimit = membersLimit;
            return this;
        }

        /**
         * Sets search tag.
         *
         * @param searchTag the search tag
         * @return the search tag
         */
        public Builder setSearchTag(String searchTag) {
            this.searchTag = searchTag;
            return this;
        }

        public Builder setMeetingIds(List<String> meetingIds) {
            this.meetingIds = meetingIds;
            return this;
        }

        public Builder setMeetingType(Integer meetingType) {
            this.meetingType = meetingType;
            return this;
        }

        public Builder setHdMeeting(Boolean hdMeeting) {
            this.hdMeeting = hdMeeting;
            return this;
        }

        public Builder setRecorded(Boolean recorded) {
            this.recorded = recorded;
            return this;
        }

        public Builder setAudioOnly(Boolean audioOnly) {
            this.audioOnly = audioOnly;
            return this;
        }

        public Builder setUserToken(String userToken) {
            this.userToken = userToken;
            return this;
        }

        /**
         * Build fetch meetings query.
         *
         * @return the fetch meetings query
         */
        public FetchMeetingsQuery build() {
            return new FetchMeetingsQuery(this);
        }
    }

    /**
     * Gets sort.
     *
     * @return the sort
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * Gets limit.
     *
     * @return the limit
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * Gets skip.
     *
     * @return the skip
     */
    public Integer getSkip() {
        return skip;
    }

    /**
     * Gets include members.
     *
     * @return the include members
     */
    public Boolean getIncludeMembers() {
        return includeMembers;
    }

    /**
     * Gets members included.
     *
     * @return the members included
     */
    public List<String> getMembersIncluded() {
        return membersIncluded;
    }

    /**
     * Gets members exactly.
     *
     * @return the members exactly
     */
    public List<String> getMembersExactly() {
        return membersExactly;
    }


    /**
     * Gets custom type.
     *
     * @return the custom type
     */
    public String getCustomType() {
        return customType;
    }


    /**
     * Gets members skip.
     *
     * @return the members skip
     */
    public Integer getMembersSkip() {
        return membersSkip;
    }

    /**
     * Gets members limit.
     *
     * @return the members limit
     */
    public Integer getMembersLimit() {
        return membersLimit;
    }

    /**
     * Gets search tag.
     *
     * @return the search tag
     */
    public String getSearchTag() {
        return searchTag;
    }

    public List<String> getMeetingIds() {
        return meetingIds;
    }

    public Integer getMeetingType() {
        return meetingType;
    }

    public Boolean getHdMeeting() {
        return hdMeeting;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public Boolean getAudioOnly() {
        return audioOnly;
    }

    public String getUserToken() {
        return userToken;
    }
}
