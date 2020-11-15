<template>
  <div ref='googleMap' class='google-map'></div>
</template>

<script>
import { getAllGps } from '../boot/axios'
export default {
    name: 'GoogleMap',
    data() {
        return {
            bounds: new google.maps.LatLngBounds(), // Авто масштабирование карты
            mapOptions: {
                center: { lat: -34.397, lng: 140.644 },
                zoomControl: true,
                zoom: 8,
                gestureHandling: 'cooperative'
            },
            imgClusterUrl: 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m',
            locations: [
                {id: 1, lat: -31.563944, lng: 147.154355, name_point: 'A', title: 'text on hover'},
                {id: 2, lat: -33.718234, lng: 150.363181, name_point: 'B', title: 'text on hover'},
                {id: 3, lat: -33.727111, lng: 150.371124, name_point: 'C', title: 'text on hover'},
                {id: 4, lat: -33.848588, lng: 151.209834, name_point: 'D', title: 'text on hover'}, 
                {id: 5, lat: -34.853202, lng: 150.55, name_point: 'E', title: 'text on hover'}, 
                {id: 6, lat: -34.851702, lng: 150.216968, name_point: 'F', title: 'text on hover'}
            ]
        }
    },
    methods: {
        initMap() {
            // create map
            const map = new google.maps.Map(this.$refs.googleMap, {
                ...this.mapOptions
            })
            
            // create Markers
            console.log(this.locations)
            let markers = this.locations.map((location) => {
                // set locations for auto zoom map
                const setLocations = new google.maps.LatLng(location.lat, location.lng)
                this.bounds.extend(setLocations)

                // set Markers on Map
                return new google.maps.Marker({
                position: location,
                map: map,
                label: location.name_point,
                title: location.title  + ' ' + location.name_point,
                })
            })

            // create MarkerClusterer and add Image
            let markerCluster = new MarkerClusterer(map, markers,
                
                { imagePath: this.imgClusterUrl })

            // авто масштабирование
            map.fitBounds(this.bounds)
        }
    },
    async mounted() {
        console.log('12')
        const res = await getAllGps()
        console.log('w', res)
        this.initMap()
    }
}
</script>

<style lang="scss">
.google-map {
  width: 100%;
  height: 100vh;
}
</style>