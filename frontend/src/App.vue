<template>
  <div id="q-app">
    <router-view />
  </div>
</template>
<script>
export default {
  name: 'App'
}
</script>

<style>
  .title {
    font-size: 32px;
    margin-top: 10px;
    margin-bottom: 10px;
  }
  .q-header {
    background: orange
  }
  body.desktop .q-focusable:focus > .q-focus-helper:after, body.desktop .q-manual-focusable--focused > .q-focus-helper:after, body.desktop .q-hoverable:hover > .q-focus-helper:after {
    background: orange;
  }
</style>