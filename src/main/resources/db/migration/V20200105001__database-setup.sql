# http://www.plumislandmedia.net/mysql/haversine-mysql-nearest-loc/

#DROP FUNCTION IF EXISTS get_distance_in_meters_between_geo_locations;

DELIMITER $$
CREATE FUNCTION get_distance_in_meters_between_geo_locations(
  geo1_latitude DOUBLE, geo1_longitude DOUBLE, 
  geo2_latitude DOUBLE, geo2_longitude DOUBLE
) 
returns decimal(6,10) DETERMINISTIC
BEGIN
  return 
  round(
    1000 * 6371 *
    acos(
        cos(
            radians(geo1_latitude)
        ) *
        cos(
            radians(geo2_latitude )
        ) *
        cos(
            radians(geo1_longitude) -
            radians(geo2_longitude)
        ) +
        sin(
            radians(geo1_latitude)
        ) *
        sin(
            radians(geo2_latitude)
        )
    )
  )
  ;
END $$
DELIMITER ;

#DROP FUNCTION IF EXISTS get_distance_in_meters_between_geo_points;
#DELIMITER $$
#CREATE FUNCTION get_distance_in_meters_between_geo_points(
#  geo1 POINT, 
#  geo2 POINT
#)
#returns decimal(10,0) DETERMINISTIC
#BEGIN
#  return ROUND(
#    1000 * 6371 *
#    acos(
#        cos(
#            radians(ST_Y(geo1))
#        ) *
#        cos(
#            radians(ST_Y(geo2))
#        ) *
#        cos(
#            radians(ST_Y(geo1)) -
#            radians(ST_Y(geo2))
#        ) +
#        sin(
#            radians(ST_X(geo1))
#        ) *
#        sin(
#            radians(ST_X(geo2))
#        )
#    )
#  );
#END $$
#DELIMITER ;