import Vue from 'vue'
import axios from 'axios'

const url = 'http://46.148.224.125:7070/api/web'
export async function getOrgs() {
    return await axios.get(url + '/all_info').then(resp => {
        return resp.data
    })
}

export async function getGPS(id) {
    return await axios.get(url + '/gps_courier/' + id).then(resp => {
        return resp.data
    })
}

export async function getAllGps() {
    return await axios.get(url + '/gps_couriers').then(resp => {
        return resp.data
    })
}
Vue.prototype.$axios = axios
