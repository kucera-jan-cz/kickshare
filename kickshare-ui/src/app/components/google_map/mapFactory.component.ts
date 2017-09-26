
/**
 * Created by KuceraJan on 21.6.2017.
 */
//Defined JS calls and types
declare var geoplugin_latitude: any;
declare var geoplugin_longitude: any;
declare var MarkerClusterer: any;

export class MapFactory {

  public createCityMap(mapId: string, center: google.maps.LatLng, readDataCallback: (...args: any[]) => void): google.maps.Map {
    if (center == null) {
      navigator.geolocation.getCurrentPosition(position => {
          console.info("Google found current position: " + JSON.stringify(position));
          const current_lat = position.coords.latitude;
          const current_lon = position.coords.longitude;
          center = new google.maps.LatLng(current_lat, current_lon);
      });
    }
    const mapOptions: google.maps.MapOptions = {
      zoom: 8,
      center: center,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    const map = new google.maps.Map(document.getElementById(mapId), mapOptions);

    map.addListener('dragend', readDataCallback);
    map.addListener('zoom_changed', readDataCallback);
    return map;
  }

  public createProjectMap(mapId: string, legendId: string, center: google.maps.LatLng, readDataCallback: (...args: any[]) => void): google.maps.Map {
    const mapOptions: google.maps.MapOptions = {
      zoom: 8,
      center: center,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    const groupGridMap = new google.maps.Map(document.getElementById(mapId), mapOptions);

    groupGridMap.data.setStyle({
      icon: '/assets/icon/gm_kickshare.png',
      fillColor: 'green'
    });

    groupGridMap.data.setStyle(function (feature) {
      var type = feature.getProperty('type');
      console.log("Type: " + type);
      return this.getIconStyle(type);
    });

    groupGridMap.addListener('dragend', readDataCallback);
    groupGridMap.addListener('zoom_changed', readDataCallback);

    var legend = document.getElementById(legendId);
    var div = document.createElement('div');
    div.innerHTML = '<img src="' + '/assets/icon/gm_kickshare_local.png' + '"> ' + 'Local' + '</img>';
    legend.appendChild(div);

    var div = document.createElement('div');
    div.innerHTML = '<img src="' + '/assets/icon/gm_kickshare.png' + '"> ' + 'Global' + '</img>';
    legend.appendChild(div);

    groupGridMap.controls[google.maps.ControlPosition.RIGHT_TOP].push(legend);
    return groupGridMap;
  }

  private createProjectClusterer(groupGridMap: google.maps.Map) {
    var clusterStyles = [
      {
        textColor: 'black',
        url: '/assets/icon/gm_kickshare_cluster.png',
        height: 45,
        width: 35,
        textSize: 16
      },
      {
        textColor: 'black',
        url: '/assets/icon/gm_kickshare_cluster.png',
        height: 45,
        width: 35,
        textSize: 16
      },
      {
        textColor: 'black',
        url: '/assets/icon/gm_kickshare_cluster.png',
        height: 45,
        width: 35,
        textSize: 16
      }
    ];
    var mcOptions = {
      gridSize: 45,
      styles: clusterStyles,
      maxZoom: 15,
      imagePath: 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m'
    };
    const markerCluster = new MarkerClusterer(groupGridMap, null, mcOptions);
    return markerCluster;
  }

  private getIconStyle(type): google.maps.Data.StyleOptions {
    console.log("Getting type: " + type);
    switch (type) {
      case "LOCAL" :
        return {
          icon: '/assets/icon/gm_kickshare_local.png',
          fillColor: 'green'
        };
      case "GLOBAL" :
        return {
          icon: '/assets/icon/gm_kickshare.png',
          fillColor: 'blue'
        };
      case "MIXED" :
        return {
          icon: '/assets/icon/gm_kickshare_both.png',
          fillColor: 'yellow'
        };
      default :
        return {
          icon: '/assets/icon/gm_kickshare_local.png',
          fillColor: 'green'
        };
    }
  }
}
