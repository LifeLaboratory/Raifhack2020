import Vue from 'vue'
import Vuex from 'vuex'
import { getOrgs } from '../boot/axios'
// import example from './module-example'

Vue.use(Vuex)

/*
 * If not building with SSR mode, you can
 * directly export the Store instantiation;
 *
 * The function below can be async too; either use
 * async/await or return a Promise which resolves
 * with the Store instance.
 */
export default new Vuex.Store({
    state: {
      orgs: []
    },

    mutations: {
      SET_ORGS: (state, payload) => {
        state.orgs = payload
      }
    },

    actions: {
      async FETCH_ORGS({commit}) {
        const data = await getOrgs()
        commit('SET_ORGS', data)
      }
    },

    getters: {
      GET_ORGS: (state, payload) => {
        return state.orgs
      }
    },
  

    // enable strict mode (adds overhead!)
    // for dev mode only
    strict: process.env.DEBUGGING
})