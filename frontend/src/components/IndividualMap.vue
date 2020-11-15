<template>
    <div>
        <div class="back" @click="back()">Назад</div>
        <div ref='googleMap' class='google-map'></div>
    </div>
</template>

<script>
import { getGPS } from '../boot/axios'
export default {
    name: 'GoogleMap',
    props: ['id'],
    data() {
        return {
            loaded: false,
            bounds: new google.maps.LatLngBounds(), // Авто масштабирование карты
            imgClusterUrl: 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m',
            locations: {},
            map: null
        }
    },
    methods: {
        back() {
            this.$emit('back')
        },
        initMap() {
            // create map
            const map = new google.maps.Map(this.$refs.googleMap, {
                center: { lat: this.locations.lat, lng: this.locations.lng},
                zoomControl: true,
                zoom: 20
            })


            new google.maps.Marker({
                position: {lat: this.locations.lat, lng: this.locations.lng},
                map,
                title: "Hello World!",
            })


            /*let markers = this.locations.map((location) => {
                // set locations for auto zoom map
                const setLocations = new google.maps.LatLng(location.lat, location.lng)
                this.bounds.extend(setLocations)

                // set Markers on Map
                return new google.maps.Marker({
                position: location,
                map: map,
                label: {
                    text: location.name_point,
                    color: 'black',
                    fontSize: "8px"
                },
                title: location.title  + ' ' + location.name_point,
                })
            })

            // create MarkerClusterer and add Image
            let markerCluster = new MarkerClusterer(map, markers,
                
                { imagePath: this.imgClusterUrl })

            // авто масштабирование
            map.fitBounds(this.bounds)*/
        }
    },
    async mounted() {
        const res = await getGPS(this.id)
        this.locations = {
            id: 1, lat: res.lat, lng: res.lon, name_point: res.name
        }
        this.initMap()
    }
}
</script>

<style lang="scss">
.google-map {
  width: 100%;
  height: 100vh;
}
.back {
    z-index: 99;
    position: absolute;
    top: 15px;
    right: 80px;
    background-color: white;
    font-size: 16px;
    border: 1px solid rgb(223, 223, 223);
    padding: 5px 15px 5px 15px
}

.back:hover {
    cursor: pointer;
}
</style>