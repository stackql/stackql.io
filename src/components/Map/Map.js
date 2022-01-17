import React from 'react';
import GoogleMapReact from 'google-map-react';

const Map = props => {
    const { mapsApiKey, lat, long, defaultZoom, markerTitle } = props;
   
    const mapProps = {
        mapsApiKey: `${mapsApiKey}`,
        center: {
          lat: lat,
          lng: long
        },
        zoom: defaultZoom,
      };
    
      const renderMarkers = (map, maps) => {
        let marker = new maps.Marker({
        position: { lat: lat, lng: long },
        map,
        title: `${markerTitle}`
        });
        return marker;
       };

    return (
        <GoogleMapReact
        bootstrapURLKeys={{ 
          key: `${mapProps.mapsApiKey}`,
          language: 'en',
          region: 'us',
        }}
        defaultCenter={mapProps.center}
        defaultZoom={mapProps.zoom}
        yesIWantToUseGoogleMapApiInternals
        onGoogleApiLoaded={({ map, maps }) => renderMarkers(map, maps)}          
      >
      </GoogleMapReact>
    );
}

export default Map;