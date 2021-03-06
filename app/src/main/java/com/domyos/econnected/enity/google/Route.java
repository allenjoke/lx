package com.domyos.econnected.enity.google;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HouWei on 16/5/21.
 */
public class Route {

    @Override
    public String toString() {
        return "Route{" +
                "routes=" + routes +
                ", geocoded_waypoints=" + geocoded_waypoints +
                ", status='" + status + '\'' +
                '}';
    }

    /**
     * routes : [{"summary":"凤城二路和明光路","copyrights":"地图数据 ©2016 GS(2011)6020","legs":[{"duration":{"text":"11分钟","value":656},"start_location":{"lng":108.9478366,"lat":34.3182252},"distance":{"text":"4.9 公里","value":4915},"start_address":"中国陕西省西安市未央区凤城二路7号 邮政编码: 710021","end_location":{"lng":108.9250205,"lat":34.3054457},"end_address":"中国陕西省西安市未央区二环路沿线商业经济带朱宏路47号 邮政编码: 710021","via_waypoint":[],"steps":[{"duration":{"text":"1分钟","value":14},"start_location":{"lng":108.9478366,"lat":34.3182252},"distance":{"text":"82 米","value":82},"travel_mode":"DRIVING","html_instructions":"向<b>东<\/b>方向，前往<b>连心路<\/b>","end_location":{"lng":108.9487233,"lat":34.3182002},"polyline":{"points":"}wmpE_{}wSBC@E?C?qA?oA"}},{"duration":{"text":"1分钟","value":12},"start_location":{"lng":108.9487233,"lat":34.3182002},"distance":{"text":"30 米","value":30},"travel_mode":"DRIVING","html_instructions":"向<b>右<\/b>转，进入<b>连心路<\/b>","end_location":{"lng":108.9487304,"lat":34.3179299},"maneuver":"turn-right","polyline":{"points":"wwmpEo`~wSt@A"}},{"duration":{"text":"5分钟","value":278},"start_location":{"lng":108.9487304,"lat":34.3179299},"distance":{"text":"1.7 公里","value":1740},"travel_mode":"DRIVING","html_instructions":"在第 1 个交叉路口向<b>右<\/b>转，进入<b>凤城二路<\/b>","end_location":{"lng":108.9297837,"lat":34.3179142},"maneuver":"turn-right","polyline":{"points":"avmpEq`~wS?l@@jF?b@ArD?F?nI?j@@vA?|L@lA?zD@pF@rBAzBEr@?jYBtU"}},{"duration":{"text":"3分钟","value":208},"start_location":{"lng":108.9297837,"lat":34.3179142},"distance":{"text":"1.8 公里","value":1816},"travel_mode":"DRIVING","html_instructions":"在<b>明光路口<\/b>向<b>左<\/b>转，进入<b>明光路<\/b>","end_location":{"lng":108.9315302,"lat":34.3021368},"maneuver":"turn-left","polyline":{"points":"}umpEcjzwS?TJAfXAnA?vDAhC?nDAfA?`@?l@?^?R?\\?`@?tF?|A?lA?dD?VFdA?rBAZ?ZALCJALCJEXK@A^URSx@w@dCcCf@e@^U^QTI\\GHCTC\\Cb@?z@?"}},{"duration":{"text":"1分钟","value":78},"start_location":{"lng":108.9315302,"lat":34.3021368},"distance":{"text":"0.8 公里","value":765},"travel_mode":"DRIVING","html_instructions":"向<b>右<\/b>转，进入<b>纬二十九街<\/b>","end_location":{"lng":108.9231978,"lat":34.3021431},"maneuver":"turn-right","polyline":{"points":"ksjpEauzwS?zF?pT?vM?zF"}},{"duration":{"text":"1分钟","value":41},"start_location":{"lng":108.9231978,"lat":34.3021431},"distance":{"text":"0.3 公里","value":348},"travel_mode":"DRIVING","html_instructions":"向<b>右<\/b>转，进入<b>朱宏路<\/b>","end_location":{"lng":108.9235787,"lat":34.3052571},"maneuver":"turn-right","polyline":{"points":"ksjpE_aywSmAIoAIEAq@EwCOc@EsBKiBM"}},{"duration":{"text":"1分钟","value":25},"start_location":{"lng":108.9235787,"lat":34.3052571},"distance":{"text":"0.1 公里","value":134},"travel_mode":"DRIVING","html_instructions":"向<b>右<\/b>转<div style=\"font-size:0.9em\">目的地在左侧<\/div>","end_location":{"lng":108.9250205,"lat":34.3054457},"maneuver":"turn-right","polyline":{"points":"{fkpEkcywS?]OcC?AU{B"}}],"traffic_speed_entry":[]}],"warnings":[],"bounds":{"southwest":{"lng":108.9231986,"lat":34.3021384},"northeast":{"lng":108.9487304,"lat":34.3182252}},"overview_polyline":{"points":"}wmpE_{}wSDI?uA?oAt@A@xGAnPBnS@lL?nFEr@?jYBjVz`@EbLAbQ?dD?VFxDAv@AXEXIZMr@i@~D{Df@e@^Ut@[f@Kr@G~A??zF?dk@oRkAOaDU}B"},"waypoint_order":[]}]
     * geocoded_waypoints : [{"types":["street_address"],"geocoder_status":"OK","place_id":"ChIJL6qx7IJ7YzYRLcFH_ROsewE"},{"types":["street_address"],"geocoder_status":"OK","place_id":"EmLkuK3lm73pmZXopb_nnIHopb_lronluILmnKrlpK7ljLrkuoznjq_ot6_msr_nur_llYbkuJrnu4_mtY7luKbmnLHlro_ot680N-WPtyDpgq7mlL_nvJbnoIE6IDcxMDAyMQ"}]
     * status : OK
     */
    @SerializedName("routes")
    private List<RoutesEntity> routes;
    @SerializedName("geocoded_waypoints")
    private List<Geocoded_waypointsEntity> geocoded_waypoints;
    @SerializedName("status")
    private String status;

    public void setRoutes(List<RoutesEntity> routes) {
        this.routes = routes;
    }

    public void setGeocoded_waypoints(List<Geocoded_waypointsEntity> geocoded_waypoints) {
        this.geocoded_waypoints = geocoded_waypoints;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RoutesEntity> getRoutes() {
        return routes;
    }

    public List<Geocoded_waypointsEntity> getGeocoded_waypoints() {
        return geocoded_waypoints;
    }

    public String getStatus() {
        return status;
    }
    /**
     * summary : 凤城二路和明光路
     * copyrights : 地图数据 ©2016 GS(2011)6020
     * legs : [{"duration":{"text":"11分钟","value":656},"start_location":{"lng":108.9478366,"lat":34.3182252},"distance":{"text":"4.9 公里","value":4915},"start_address":"中国陕西省西安市未央区凤城二路7号 邮政编码: 710021","end_location":{"lng":108.9250205,"lat":34.3054457},"end_address":"中国陕西省西安市未央区二环路沿线商业经济带朱宏路47号 邮政编码: 710021","via_waypoint":[],"steps":[{"duration":{"text":"1分钟","value":14},"start_location":{"lng":108.9478366,"lat":34.3182252},"distance":{"text":"82 米","value":82},"travel_mode":"DRIVING","html_instructions":"向<b>东<\/b>方向，前往<b>连心路<\/b>","end_location":{"lng":108.9487233,"lat":34.3182002},"polyline":{"points":"}wmpE_{}wSBC@E?C?qA?oA"}},{"duration":{"text":"1分钟","value":12},"start_location":{"lng":108.9487233,"lat":34.3182002},"distance":{"text":"30 米","value":30},"travel_mode":"DRIVING","html_instructions":"向<b>右<\/b>转，进入<b>连心路<\/b>","end_location":{"lng":108.9487304,"lat":34.3179299},"maneuver":"turn-right","polyline":{"points":"wwmpEo`~wSt@A"}},{"duration":{"text":"5分钟","value":278},"start_location":{"lng":108.9487304,"lat":34.3179299},"distance":{"text":"1.7 公里","value":1740},"travel_mode":"DRIVING","html_instructions":"在第 1 个交叉路口向<b>右<\/b>转，进入<b>凤城二路<\/b>","end_location":{"lng":108.9297837,"lat":34.3179142},"maneuver":"turn-right","polyline":{"points":"avmpEq`~wS?l@@jF?b@ArD?F?nI?j@@vA?|L@lA?zD@pF@rBAzBEr@?jYBtU"}},{"duration":{"text":"3分钟","value":208},"start_location":{"lng":108.9297837,"lat":34.3179142},"distance":{"text":"1.8 公里","value":1816},"travel_mode":"DRIVING","html_instructions":"在<b>明光路口<\/b>向<b>左<\/b>转，进入<b>明光路<\/b>","end_location":{"lng":108.9315302,"lat":34.3021368},"maneuver":"turn-left","polyline":{"points":"}umpEcjzwS?TJAfXAnA?vDAhC?nDAfA?`@?l@?^?R?\\?`@?tF?|A?lA?dD?VFdA?rBAZ?ZALCJALCJEXK@A^URSx@w@dCcCf@e@^U^QTI\\GHCTC\\Cb@?z@?"}},{"duration":{"text":"1分钟","value":78},"start_location":{"lng":108.9315302,"lat":34.3021368},"distance":{"text":"0.8 公里","value":765},"travel_mode":"DRIVING","html_instructions":"向<b>右<\/b>转，进入<b>纬二十九街<\/b>","end_location":{"lng":108.9231978,"lat":34.3021431},"maneuver":"turn-right","polyline":{"points":"ksjpEauzwS?zF?pT?vM?zF"}},{"duration":{"text":"1分钟","value":41},"start_location":{"lng":108.9231978,"lat":34.3021431},"distance":{"text":"0.3 公里","value":348},"travel_mode":"DRIVING","html_instructions":"向<b>右<\/b>转，进入<b>朱宏路<\/b>","end_location":{"lng":108.9235787,"lat":34.3052571},"maneuver":"turn-right","polyline":{"points":"ksjpE_aywSmAIoAIEAq@EwCOc@EsBKiBM"}},{"duration":{"text":"1分钟","value":25},"start_location":{"lng":108.9235787,"lat":34.3052571},"distance":{"text":"0.1 公里","value":134},"travel_mode":"DRIVING","html_instructions":"向<b>右<\/b>转<div style=\"font-size:0.9em\">目的地在左侧<\/div>","end_location":{"lng":108.9250205,"lat":34.3054457},"maneuver":"turn-right","polyline":{"points":"{fkpEkcywS?]OcC?AU{B"}}],"traffic_speed_entry":[]}]
     * warnings : []
     * bounds : {"southwest":{"lng":108.9231986,"lat":34.3021384},"northeast":{"lng":108.9487304,"lat":34.3182252}}
     * overview_polyline : {"points":"}wmpE_{}wSDI?uA?oAt@A@xGAnPBnS@lL?nFEr@?jYBjVz`@EbLAbQ?dD?VFxDAv@AXEXIZMr@i@~D{Df@e@^Ut@[f@Kr@G~A??zF?dk@oRkAOaDU}B"}
     * waypoint_order : []
     */
    public class RoutesEntity {

        @SerializedName("summary")
        private String summary;
        @SerializedName("copyrights")
        private String copyrights;
        @SerializedName("legs")
        private List<LegsEntity> legs;
        @SerializedName("warnings")
        private List<?> warnings;
        @SerializedName("bounds")
        private BoundsEntity bounds;
        @SerializedName("overview_polyline")
        private Overview_polylineEntity overview_polyline;
        @SerializedName("waypoint_order")
        private List<?> waypoint_order;

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public void setCopyrights(String copyrights) {
            this.copyrights = copyrights;
        }

        public void setLegs(List<LegsEntity> legs) {
            this.legs = legs;
        }

        public void setWarnings(List<?> warnings) {
            this.warnings = warnings;
        }

        public void setBounds(BoundsEntity bounds) {
            this.bounds = bounds;
        }

        public void setOverview_polyline(Overview_polylineEntity overview_polyline) {
            this.overview_polyline = overview_polyline;
        }

        public void setWaypoint_order(List<?> waypoint_order) {
            this.waypoint_order = waypoint_order;
        }

        public String getSummary() {
            return summary;
        }

        public String getCopyrights() {
            return copyrights;
        }

        public List<LegsEntity> getLegs() {
            return legs;
        }

        public List<?> getWarnings() {
            return warnings;
        }

        public BoundsEntity getBounds() {
            return bounds;
        }

        public Overview_polylineEntity getOverview_polyline() {
            return overview_polyline;
        }

        public List<?> getWaypoint_order() {
            return waypoint_order;
        }
        /**
         * duration : {"text":"11分钟","value":656}
         * start_location : {"lng":108.9478366,"lat":34.3182252}
         * distance : {"text":"4.9 公里","value":4915}
         * start_address : 中国陕西省西安市未央区凤城二路7号 邮政编码: 710021
         * end_location : {"lng":108.9250205,"lat":34.3054457}
         * end_address : 中国陕西省西安市未央区二环路沿线商业经济带朱宏路47号 邮政编码: 710021
         * via_waypoint : []
         * steps : [{"duration":{"text":"1分钟","value":14},"start_location":{"lng":108.9478366,"lat":34.3182252},"distance":{"text":"82 米","value":82},"travel_mode":"DRIVING","html_instructions":"向<b>东<\/b>方向，前往<b>连心路<\/b>","end_location":{"lng":108.9487233,"lat":34.3182002},"polyline":{"points":"}wmpE_{}wSBC@E?C?qA?oA"}},{"duration":{"text":"1分钟","value":12},"start_location":{"lng":108.9487233,"lat":34.3182002},"distance":{"text":"30 米","value":30},"travel_mode":"DRIVING","html_instructions":"向<b>右<\/b>转，进入<b>连心路<\/b>","end_location":{"lng":108.9487304,"lat":34.3179299},"maneuver":"turn-right","polyline":{"points":"wwmpEo`~wSt@A"}},{"duration":{"text":"5分钟","value":278},"start_location":{"lng":108.9487304,"lat":34.3179299},"distance":{"text":"1.7 公里","value":1740},"travel_mode":"DRIVING","html_instructions":"在第 1 个交叉路口向<b>右<\/b>转，进入<b>凤城二路<\/b>","end_location":{"lng":108.9297837,"lat":34.3179142},"maneuver":"turn-right","polyline":{"points":"avmpEq`~wS?l@@jF?b@ArD?F?nI?j@@vA?|L@lA?zD@pF@rBAzBEr@?jYBtU"}},{"duration":{"text":"3分钟","value":208},"start_location":{"lng":108.9297837,"lat":34.3179142},"distance":{"text":"1.8 公里","value":1816},"travel_mode":"DRIVING","html_instructions":"在<b>明光路口<\/b>向<b>左<\/b>转，进入<b>明光路<\/b>","end_location":{"lng":108.9315302,"lat":34.3021368},"maneuver":"turn-left","polyline":{"points":"}umpEcjzwS?TJAfXAnA?vDAhC?nDAfA?`@?l@?^?R?\\?`@?tF?|A?lA?dD?VFdA?rBAZ?ZALCJALCJEXK@A^URSx@w@dCcCf@e@^U^QTI\\GHCTC\\Cb@?z@?"}},{"duration":{"text":"1分钟","value":78},"start_location":{"lng":108.9315302,"lat":34.3021368},"distance":{"text":"0.8 公里","value":765},"travel_mode":"DRIVING","html_instructions":"向<b>右<\/b>转，进入<b>纬二十九街<\/b>","end_location":{"lng":108.9231978,"lat":34.3021431},"maneuver":"turn-right","polyline":{"points":"ksjpEauzwS?zF?pT?vM?zF"}},{"duration":{"text":"1分钟","value":41},"start_location":{"lng":108.9231978,"lat":34.3021431},"distance":{"text":"0.3 公里","value":348},"travel_mode":"DRIVING","html_instructions":"向<b>右<\/b>转，进入<b>朱宏路<\/b>","end_location":{"lng":108.9235787,"lat":34.3052571},"maneuver":"turn-right","polyline":{"points":"ksjpE_aywSmAIoAIEAq@EwCOc@EsBKiBM"}},{"duration":{"text":"1分钟","value":25},"start_location":{"lng":108.9235787,"lat":34.3052571},"distance":{"text":"0.1 公里","value":134},"travel_mode":"DRIVING","html_instructions":"向<b>右<\/b>转<div style=\"font-size:0.9em\">目的地在左侧<\/div>","end_location":{"lng":108.9250205,"lat":34.3054457},"maneuver":"turn-right","polyline":{"points":"{fkpEkcywS?]OcC?AU{B"}}]
         * traffic_speed_entry : []
         */
        public class LegsEntity {

            @SerializedName("duration")
            private DurationEntity duration;
            @SerializedName("start_location")
            private Start_locationEntity start_location;
            @SerializedName("distance")
            private DistanceEntity distance;
            @SerializedName("start_address")
            private String start_address;
            @SerializedName("end_location")
            private End_locationEntity end_location;
            @SerializedName("end_address")
            private String end_address;
            @SerializedName("via_waypoint")
            private List<?> via_waypoint;
            @SerializedName("steps")
            private List<StepsEntity> steps;
            @SerializedName("traffic_speed_entry")
            private List<?> traffic_speed_entry;

            public void setDuration(DurationEntity duration) {
                this.duration = duration;
            }

            public void setStart_location(Start_locationEntity start_location) {
                this.start_location = start_location;
            }

            public void setDistance(DistanceEntity distance) {
                this.distance = distance;
            }

            public void setStart_address(String start_address) {
                this.start_address = start_address;
            }

            public void setEnd_location(End_locationEntity end_location) {
                this.end_location = end_location;
            }

            public void setEnd_address(String end_address) {
                this.end_address = end_address;
            }

            public void setVia_waypoint(List<?> via_waypoint) {
                this.via_waypoint = via_waypoint;
            }

            public void setSteps(List<StepsEntity> steps) {
                this.steps = steps;
            }

            public void setTraffic_speed_entry(List<?> traffic_speed_entry) {
                this.traffic_speed_entry = traffic_speed_entry;
            }

            public DurationEntity getDuration() {
                return duration;
            }

            public Start_locationEntity getStart_location() {
                return start_location;
            }

            public DistanceEntity getDistance() {
                return distance;
            }

            public String getStart_address() {
                return start_address;
            }

            public End_locationEntity getEnd_location() {
                return end_location;
            }

            public String getEnd_address() {
                return end_address;
            }

            public List<?> getVia_waypoint() {
                return via_waypoint;
            }

            public List<StepsEntity> getSteps() {
                return steps;
            }

            public List<?> getTraffic_speed_entry() {
                return traffic_speed_entry;
            }

            public class DurationEntity {
                /**
                 * text : 11分钟
                 * value : 656
                 */
                @SerializedName("text")
                private String text;
                @SerializedName("value")
                private int value;

                public void setText(String text) {
                    this.text = text;
                }

                public void setValue(int value) {
                    this.value = value;
                }

                public String getText() {
                    return text;
                }

                public int getValue() {
                    return value;
                }
            }

            public class Start_locationEntity {
                /**
                 * lng : 108.9478366
                 * lat : 34.3182252
                 */
                @SerializedName("lng")
                private double lng;
                @SerializedName("lat")
                private double lat;

                public void setLng(double lng) {
                    this.lng = lng;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public double getLat() {
                    return lat;
                }
            }

            public class DistanceEntity {
                /**
                 * text : 4.9 公里
                 * value : 4915
                 */
                @SerializedName("text")
                private String text;
                @SerializedName("value")
                private int value;

                public void setText(String text) {
                    this.text = text;
                }

                public void setValue(int value) {
                    this.value = value;
                }

                public String getText() {
                    return text;
                }

                public int getValue() {
                    return value;
                }
            }

            public class End_locationEntity {
                /**
                 * lng : 108.9250205
                 * lat : 34.3054457
                 */
                @SerializedName("lng")
                private double lng;
                @SerializedName("lat")
                private double lat;

                public void setLng(double lng) {
                    this.lng = lng;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public double getLat() {
                    return lat;
                }
            }
            /**
             * duration : {"text":"1分钟","value":14}
             * start_location : {"lng":108.9478366,"lat":34.3182252}
             * distance : {"text":"82 米","value":82}
             * travel_mode : DRIVING
             * html_instructions : 向<b>东</b>方向，前往<b>连心路</b>
             * end_location : {"lng":108.9487233,"lat":34.3182002}
             * polyline : {"points":"}wmpE_{}wSBC@E?C?qA?oA"}
             */
            public class StepsEntity {

                @SerializedName("duration")
                private DurationEntity duration;
                @SerializedName("start_location")
                private Start_locationEntity start_location;
                @SerializedName("distance")
                private DistanceEntity distance;
                @SerializedName("travel_mode")
                private String travel_mode;
                @SerializedName("html_instructions")
                private String html_instructions;
                @SerializedName("end_location")
                private End_locationEntity end_location;
                @SerializedName("polyline")
                private PolylineEntity polyline;

                public void setDuration(DurationEntity duration) {
                    this.duration = duration;
                }

                public void setStart_location(Start_locationEntity start_location) {
                    this.start_location = start_location;
                }

                public void setDistance(DistanceEntity distance) {
                    this.distance = distance;
                }

                public void setTravel_mode(String travel_mode) {
                    this.travel_mode = travel_mode;
                }

                public void setHtml_instructions(String html_instructions) {
                    this.html_instructions = html_instructions;
                }

                public void setEnd_location(End_locationEntity end_location) {
                    this.end_location = end_location;
                }

                public void setPolyline(PolylineEntity polyline) {
                    this.polyline = polyline;
                }

                public DurationEntity getDuration() {
                    return duration;
                }

                public Start_locationEntity getStart_location() {
                    return start_location;
                }

                public DistanceEntity getDistance() {
                    return distance;
                }

                public String getTravel_mode() {
                    return travel_mode;
                }

                public String getHtml_instructions() {
                    return html_instructions;
                }

                public End_locationEntity getEnd_location() {
                    return end_location;
                }

                public PolylineEntity getPolyline() {
                    return polyline;
                }

                public class DurationEntity {
                    /**
                     * text : 1分钟
                     * value : 14
                     */
                    @SerializedName("text")
                    private String text;
                    @SerializedName("value")
                    private int value;

                    public void setText(String text) {
                        this.text = text;
                    }

                    public void setValue(int value) {
                        this.value = value;
                    }

                    public String getText() {
                        return text;
                    }

                    public int getValue() {
                        return value;
                    }
                }

                public class Start_locationEntity {
                    /**
                     * lng : 108.9478366
                     * lat : 34.3182252
                     */
                    @SerializedName("lng")
                    private double lng;
                    @SerializedName("lat")
                    private double lat;

                    public void setLng(double lng) {
                        this.lng = lng;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public double getLat() {
                        return lat;
                    }
                }

                public class DistanceEntity {
                    /**
                     * text : 82 米
                     * value : 82
                     */
                    @SerializedName("text")
                    private String text;
                    @SerializedName("value")
                    private int value;

                    public void setText(String text) {
                        this.text = text;
                    }

                    public void setValue(int value) {
                        this.value = value;
                    }

                    public String getText() {
                        return text;
                    }

                    public int getValue() {
                        return value;
                    }
                }

                public class End_locationEntity {
                    /**
                     * lng : 108.9487233
                     * lat : 34.3182002
                     */
                    @SerializedName("lng")
                    private double lng;
                    @SerializedName("lat")
                    private double lat;

                    public void setLng(double lng) {
                        this.lng = lng;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public double getLat() {
                        return lat;
                    }
                }

                public class PolylineEntity {
                    /**
                     * points : }wmpE_{}wSBC@E?C?qA?oA
                     */
                    @SerializedName("points")
                    private String points;

                    public void setPoints(String points) {
                        this.points = points;
                    }

                    public String getPoints() {
                        return points;
                    }
                }
            }
        }

        public class BoundsEntity {
            /**
             * southwest : {"lng":108.9231986,"lat":34.3021384}
             * northeast : {"lng":108.9487304,"lat":34.3182252}
             */
            @SerializedName("southwest")
            private SouthwestEntity southwest;
            @SerializedName("northeast")
            private NortheastEntity northeast;

            public void setSouthwest(SouthwestEntity southwest) {
                this.southwest = southwest;
            }

            public void setNortheast(NortheastEntity northeast) {
                this.northeast = northeast;
            }

            public SouthwestEntity getSouthwest() {
                return southwest;
            }

            public NortheastEntity getNortheast() {
                return northeast;
            }

            public class SouthwestEntity {
                /**
                 * lng : 108.9231986
                 * lat : 34.3021384
                 */
                @SerializedName("lng")
                private double lng;
                @SerializedName("lat")
                private double lat;

                public void setLng(double lng) {
                    this.lng = lng;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public double getLat() {
                    return lat;
                }
            }

            public class NortheastEntity {
                /**
                 * lng : 108.9487304
                 * lat : 34.3182252
                 */
                @SerializedName("lng")
                private double lng;
                @SerializedName("lat")
                private double lat;

                public void setLng(double lng) {
                    this.lng = lng;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public double getLat() {
                    return lat;
                }
            }
        }

        public class Overview_polylineEntity {
            /**
             * points : }wmpE_{}wSDI?uA?oAt@A@xGAnPBnS@lL?nFEr@?jYBjVz`@EbLAbQ?dD?VFxDAv@AXEXIZMr@i@~D{Df@e@^Ut@[f@Kr@G~A??zF?dk@oRkAOaDU}B
             */
            @SerializedName("points")
            private String points;

            public void setPoints(String points) {
                this.points = points;
            }

            public String getPoints() {
                return points;
            }
        }
    }

    public class Geocoded_waypointsEntity {
        /**
         * types : ["street_address"]
         * geocoder_status : OK
         * place_id : ChIJL6qx7IJ7YzYRLcFH_ROsewE
         */
        @SerializedName("types")
        private List<String> types;
        @SerializedName("geocoder_status")
        private String geocoder_status;
        @SerializedName("place_id")
        private String place_id;

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public void setGeocoder_status(String geocoder_status) {
            this.geocoder_status = geocoder_status;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public List<String> getTypes() {
            return types;
        }

        public String getGeocoder_status() {
            return geocoder_status;
        }

        public String getPlace_id() {
            return place_id;
        }
    }
}
