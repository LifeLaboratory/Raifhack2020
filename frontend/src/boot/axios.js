import Vue from 'vue'
import axios from 'axios'

const url = 'http://46.148.224.125:7070/api'
export async function getOrgs() {
    return await axios.get(url + '/web/all_info').then(resp => {
        return resp.data
    })
}

export async function getGPS(id) {
    return await axios.get(url + '/web/gps_courier/' + id).then(resp => {
        return resp.data
    })
}

export async function getAllGps() {
    return await axios.get(url + '/web/gps_couriers').then(resp => {
        return resp.data
    })
}

export async function getClients() {
    return await axios.get(url + '/clients').then(resp => {
        return resp.data
    })
}

export async function getCouriers() {
    return await axios.get(url + '/couriers').then(resp => {
        return resp.data
    })
}
Vue.prototype.$axios = axios