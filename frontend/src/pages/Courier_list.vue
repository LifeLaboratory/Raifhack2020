<template>
  <q-layout view="lHh Lpr lFf">
    <q-page-container >
        <div class="title">Курьеры</div>
        <q-list bordered class="rounded-borders">
        <div v-for="(el, key) in courers" :key="key">
            <q-item clickable v-ripple @click="goToMap(el.id)">
                <q-item-section avatar>
                <q-avatar>
                    <img src="https://cdn.quasar.dev/img/avatar2.jpg">
                </q-avatar>
                </q-item-section>

                <q-item-section>
                <q-item-label lines="1">{{el.name}}</q-item-label>
                <q-item-label caption lines="2">
                    <span class="text-weight-bold">Janet</span>
                    -- I'll be in your neighborhood doing errands this
                    weekend. Do you want to grab brunch?
                </q-item-label>
                </q-item-section>

                <q-item-section side top>
                1 min ago
                </q-item-section>
            </q-item>
            <q-separator inset="item" />
        </div>
        </q-list>
    </q-page-container>
  </q-layout>
</template>

<script>
export default {
  name: 'PageIndex',
  data () {
    return {
        courers: [
            {
                id: 1,
                name: 'Путин Михаил',
                status: 'Работает'
            }
        ]
    }
  },
  methods: {
      goToMap(id) {
          this.$router.push({path: 'couer/detail_map/' + id})
      }
  }
}
</script>

<style scoped>
    .container {
        width: 95%;
        margin-left: 2.5%;
    }
</style>