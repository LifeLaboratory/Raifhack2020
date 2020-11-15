<template>
    <div>
        <div class="couirers">
            Занятых курьеров:<br>
            Свободных курьеров:
        </div>
        <div ref='googleMap' class='google-map'></div>
    </div>
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
            locations: [ ]
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
        const res = await getAllGps()
        res.forEach((el, key) => {
            console.log(el)
            this.locations.push({
                id: key,
                lat: el.lat,
                lng: el.lon,
                name_point: el.name_point,
                title: el.title
            })
        });
        this.initMap()
    }
}
</script>

<style>
    .couirers {
        padding: 9px;
        font-size: 20px;
        z-index: 99;
        bottom: 30px;
        right: 60px;
        background: white;
        position: absolute;
    }

    .google-map {
        z-index: 1;
        width: 100%;
        height: 94vh;
    }
</style>