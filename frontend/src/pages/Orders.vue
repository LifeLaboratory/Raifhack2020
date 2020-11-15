<template>
  <q-layout view="lHh Lpr lFf">
    <q-page-container >
        <div class="title">Заказы  <q-btn color="primary" label="Добавить заказ"  style="margin-left: 20px" @click="alert = true"/></div>
        <q-dialog v-model="alert">
            <q-card style="width: 100%">
                <q-card-section>
                <div class="text-h6">Alert</div>
                </q-card-section>

                <q-card-section class="q-pt-none">
                    <q-input
                        filled
                        v-model="address"
                        label="Адресс доставки *"
                        lazy-rules
                    />
                    <br />
                    <q-input
                        filled
                        v-model="cost"
                        label="Цена *"
                        lazy-rules
                    />
                    <br />
                    <q-select filled v-model="client" :options="optionsClient" label="Клиент" />
                    <br />
                    <q-select filled v-model="couirer" :options="optionsCour" label="Курьер" />
                </q-card-section>

                <q-card-actions align="right">
                <q-btn flat label="OK" color="primary" v-close-popup />
                </q-card-actions>
            </q-card>
        </q-dialog>

    </q-page-container>
  </q-layout>
</template>

<script>
import {getClients, getCouriers} from '../boot/axios'
export default {
  data () {
    return {
        address: '',
        cost: '',
        client: '',
        couirer: '',
        optionsClient: [],
        optionsCour: [],

        alert: false
    }
  },
  methods: {
       
  },
  async mounted() {
        const cli = await getClients()
        const cou = await getCouriers()
        console.log(cli)
        this.optionsClient = cli.map((el) => {
            return el.name
        })
        this.optionsCour = cou.map((el) => {
            if (el.status === false) return el.name
        })
        console.log(this.optionsCour)
        console.log(this.optionsClient.filter(el => el !== undefined))
      
  }
}
</script>

<style scoped>
    .title {
        padding-left: 20px;
    }
</style>