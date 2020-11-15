<template>
  <q-layout view="lHh Lpr lFf" v-if='uploaded'>
    <template v-if="show === 'company'">
        <div class="title" style="margin-left: 10px">Компании</div>
        <q-list bordered separator>
            <q-item clickable v-ripple v-for="(el, key) in courers" :key="key" @click="showData(el.data, 'couriers', el.company)">
                <q-item-section>
                <q-item-label></q-item-label>
                <q-item-label>{{el.name}}</q-item-label>
                </q-item-section>
            </q-item>
        </q-list>
    </template>

    <template v-else-if="show === 'couriers'">
        <div class="title" style="margin-left: 10px">Курьеры компании - {{titleName}} <q-btn color="white" text-color="black" label="Назад" @click="back('company')"/> </div>
        <q-list bordered separator>
            <div v-for="(el, key) in data" :key="key" class="block" style="z-index: 1">
                <q-item>
                    <q-item-section>
                        <q-item-label><b>{{key + 1}}. {{el.surname}} {{el.name}}</b></q-item-label>
                        <q-item-label>Телефон - {{el.phone}}</q-item-label>
                    </q-item-section>
                    <q-item-section style="text-align: right">
                        <div v-if="el.status === null">
                            <q-badge class="status" style="background-color: #50b550">
                                Не работает
                            </q-badge>
                        </div>
                        <div v-if="el.status === false">
                            <q-badge class="status" style="background-color: orange">
                                Свободен
                            </q-badge>
                        </div>
                        <div v-if="el.status === true">
                            <q-badge class="status" color="error">
                                Работает
                            </q-badge>
                        </div>
                    </q-item-section>
                </q-item>
                <q-btn color="primary" label="Просмотреть местоположение" style="margin-left: 15px; font-size: 10px; margin-top: 10px;" v-if="el.status === true" @click="openMap(el.id)" />
                <q-expansion-item
                    v-model="ordersAC[key]"
                    label="Заказы"
                    style="margin-top: 20px"
                    >
                    <q-card>
                        <q-card-section>
                            <div v-for="(order, key_order) in el.orders" :key="key_order" style="width: 80%; margin-left: 10%">
                                <q-item style="font-size: 12px">
                                    <q-item-section>
                                        <q-item-label><b>{{key_order + 1}}. {{order.address}}</b></q-item-label>
                                        <q-item-label>{{order.cost}} руб.</q-item-label>
                                    </q-item-section>
                                    <q-item-section style="text-align: right">
                                        <div v-if="order.status_order === NaN">
                                            <q-badge class="status" style="background-color: orange; font-size: 12px">
                                                В процессе
                                            </q-badge>
                                        </div>
                                        <div v-if="order.status_order">
                                            <q-badge class="status" style="background-color: #50b550; font-size: 12px">
                                                Выполнен
                                            </q-badge>
                                        </div>
                                        <div v-if="!order.status_order">
                                            <q-badge class="status" style="background-color: #b30404f2; font-size: 12px">
                                                Отменен
                                            </q-badge>
                                        </div>
                                    </q-item-section>
                                </q-item>
                            </div>
                        </q-card-section>
                    </q-card>
                </q-expansion-item>
            </div>
        </q-list>
    </template>
    <template v-else-if="show === 'map'">
        <IndividualMap :id="courerID" @back="show = 'couriers'" />
    </template>
  </q-layout>
</template>

<script>
import IndividualMap from "../components/IndividualMap"
export default {
  name: 'PageIndex',
  components: {
      IndividualMap
  },
  data () {
    return {
        ordersAC: [0,0,0,0,0,0,0,0,0,0,0,0,0,0],
        data: {},
        titleName: '',
        uploaded: false,
        courers: [],
        show: 'company',
        courerID: null
    }
  },
  methods: {
    showOrders (key) {
        this.ordersAC[key] = true
        console.log(this.ordersAC)
    },
    openMap (id) {
        console.log(id)
        this.courerID = id
        this.show = 'map'
    },
    back(name) {
        this.show = name
    },
    showData (data, show, company = null) {
        console.log('sad')
        if (company) this.titleName = company
        this.data = data
        this.show = show
    }
  },
  async mounted() {
      const res = await this.$store.dispatch('FETCH_ORGS')
      this.courers = this.$store.state.orgs
      this.uploaded = true
  },
  watch: {
      ordersAC: (el) => {
          console.log('w', el)
      }
  }
}
</script>

<style scoped>
    .container {
        width: 95%;
        margin-left: 2.5%;
    }
    .status {
        font-size: 14px;
        font-weight: bold;
        padding: 7px;
    }
    .block  {
        margin-bottom: 20px;
        padding-bottom: 15px;
        border-bottom: 1px solid black;
    }
</style>