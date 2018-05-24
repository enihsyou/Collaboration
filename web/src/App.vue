<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
  export default {
    name: 'App'
  }
</script>

<style lang="stylus">
  [v-cloak]
    display none

  html, body, #app
    width 100%
    height 100%
    overflow hidden

  body
    margin 0
    padding 0

  a
    color white
    &:hover
      color ghostwhite
    &:visited
      color ghostwhite

  #app
    font-family: 'Avenir', Helvetica, Arial, sans-serif
    -webkit-font-smoothing: antialiased
    -moz-osx-font-smoothing: grayscale

  .untouchable
    -webkit-touch-callout none
    -webkit-user-select none
    -moz-user-select none
    -ms-user-select none
    user-select none

  .left
    display inline
    float left

  .right
    display inline
    float right

  .center
    clear both
    display block
    margin auto

</style>
