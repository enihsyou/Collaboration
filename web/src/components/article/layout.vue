<template>
  <el-container>
    <!--title-->
    <el-header height="4em">
      <img class="left logo" src="../../assets/img/img_logo.png" alt="" draggable="false">
      <h1 class="title untouchable">NTM协同文档</h1>
    </el-header>
    <el-container>
      <!--nav-->
      <!--hideNav-->
      <el-button id="toggle-nav" :class="{'show-nav':!navHasHide,'hide-nav':navHasHide}"
                 :icon="navHasHide?'el-icon-arrow-right':'el-icon-arrow-left'"
                 type="text" @click="toggleNav" circle>
      </el-button>
      <el-aside v-show="!navHasHide" width="13em">
        <el-menu :default-openeds="['myArticles','otherArticles']"
                 :default-active="currenActiveItem">
          <!--draftArticle-->
          <el-button class="draftArticle center" size="large" type="primary" icon="el-icon-plus"
                     @click="draftArticle">
            新建文章
          </el-button>
          <!--我的文章-->
          <el-submenu index="myArticles">
            <template slot="title">
              <i class="el-icon-document"></i>
              <span>我的文章</span>
            </template>
            <router-link v-for="paper in myArticles" :key="paper.id" :to="`/user/${paper.id}`">
              <el-menu-item :index="paper.id">
                {{paper.title}}
              </el-menu-item>
            </router-link>
          </el-submenu>
          <!--我参与的文章-->
          <el-submenu index="otherArticles">
            <template slot="title">
              <i class="el-icon-tickets"></i>
              <span>我参与的文章</span>
            </template>
            <router-link v-for="paper in otherArticles" :key="paper.id" :to="`/user/${paper.id}`">
              <el-menu-item :index="paper.id">
                {{paper.title}}
              </el-menu-item>
            </router-link>
          </el-submenu>
        </el-menu>
      </el-aside>
      <!--Main-->
      <el-main>
        <router-view/>
      </el-main>
    </el-container>
    <!--footer-->
    <el-footer class="untouchable" height="3em">
      Design By sleaf
    </el-footer>
  </el-container>
</template>

<script>
  export default {
    name: "layout",
    data() {
      return {
        currenActiveItem: '',
        navHasHide: false,
        myArticles: [
          {
            id: 'some_hex_string',
            title: '我的文章1'
          }
        ],
        otherArticles: [
          {
            id: 'some_hex_string2',
            title: '他的文章2'
          }
        ]
      }
    },
    methods: {
      draftArticle() {
        let random = Math.random().toString(16).slice(8);
        this.myArticles.push({
          id: random,
          title: '我的文章_' + random
        });
        this.currenActiveItem = random;
        this.$router.push(random)
      },
      hideNav() {
        this.navHasHide = true;
      },
      showNav() {
        this.navHasHide = false;
      },
      toggleNav() {
        this.navHasHide = !this.navHasHide;
      },
    },
    mounted() {

    }
  }
</script>

<style lang="stylus" scoped>
  .el-container
    width 100%
    height 100%
    padding 0
    margin 0

  .el-header
    background-color: white
    color: #333
    line-height: 2em
    box-shadow 0 2px 5px grey
    z-index 99
    overflow hidden
    .logo
      height 3em
      margin .5em
      border-radius 1em

  .el-aside
    color: #333
    text-align: left
    background-color: #E9EEF3
    overflow-x hidden
    transition all 500ms ease-in-out 0s
    .draftArticle
      margin-top 1.5em
      margin-bottom .5em

    &.hide-nav
      left 1em
      background rgba(200, 200, 200, 0.66)

    &.show-nav
      left 0

  #toggle-nav
    position absolute
    z-index 99999
    transition all 500ms ease-in-out 0s
    top 6em
    &.hide-nav
      left 1em
      background rgba(200, 200, 200, 0.66)

    &.show-nav
      left 12em
      color black
      &:hover
        background rgba(200, 200, 200, 0.33)

  .el-main
    color: #333
    text-align: left
    height 100% -7em
    background-color: #dfe4e9
    overflow auto
    display flex
    justify-content center

  .el-footer
    background-color: white
    color: #333
    box-shadow 0 -2px 1px grey;
    text-align center
    line-height: 3em
    z-index 99

</style>
