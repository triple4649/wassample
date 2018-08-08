FROM websphere-liberty
COPY ./build/libs/MyWebApp.war /config/dropins/MyWebApp.war
COPY ./pdf /pdf