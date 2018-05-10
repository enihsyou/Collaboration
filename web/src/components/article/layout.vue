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
      <el-aside :class="{'show-nav':!navHasHide,'hide-nav':navHasHide}" width="13em">
        <el-menu :default-openeds="['myArticles','otherArticles']"
                 :default-active="currenActiveItem">
          <!--draftArticle-->
          <el-button class="draftArticle center" size="large" type="primary" icon="el-icon-plus"
                     @click="draftArticle">
            新建文章
          </el-button>
          <!--我的文章-->
          <el-submenu class="submenu" index="myArticles">
            <template slot="title">
              <i class="el-icon-document"></i>
              <span>我的文章</span>
            </template>
            <el-menu-item class="submenu-item" v-for="paper in myArticles" :key="paper.id" :index="paper.id"
                          @click="changeArticle(paper)">
              {{paper.title}}
            </el-menu-item>
          </el-submenu>
          <!--我参与的文章-->
          <el-submenu class="submenu" index="otherArticles">
            <template slot="title">
              <i class="el-icon-tickets"></i>
              <span>我参与的文章</span>
            </template>
            <el-menu-item class="submenu-item" v-for="paper in otherArticles" :key="paper.id" :index="paper.id"
                          @click="changeArticle(paper)">
              {{paper.title}}
            </el-menu-item>
          </el-submenu>
        </el-menu>
        <el-button class="btn_logout" type="danger" @click="logout">
          <i class="el-icon-back"></i>
          &#12288;退出登录
        </el-button>
      </el-aside>
      <!--Main-->
      <el-main :class="{'show-nav':!navHasHide,'hide-nav':navHasHide}">
        <transition name="el-zoom-in-top" mode="out-in" appear>
          <router-view :key="currentArticle.id"/>
        </transition>
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
        ],
        currentArticle: {}
      }
    },
    methods: {
      draftArticle() {
        let random = Math.random().toString(16).slice(8);
        this.currentArticle = {
          id: random,
          title: '我的文章_' + random
        };
        this.myArticles.push(this.currentArticle);
        this.currenActiveItem = random;
        this.$router.push(`/user/${random}`)
      },
      changeArticle(currentArticle) {
        this.currentArticle = currentArticle;
        this.$router.push(`/user/${this.currentArticle.id}`);
        console.log(`跳转到文章：${this.currentArticle.id}:${this.currentArticle.title}`);
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
      logout() {
        delete sessionStorage.token;
        this.$message.success('登出成功');
        this.$router.push('/login');
      }
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
    position relative
    color: #333
    text-align: left
    background-color: white
    overflow-x hidden
    transition all 500ms ease-in-out 0s
    box-shadow 2px 0 1px #50626e
    .submenu-item
      box-shadow 1px 1px 1px 1px #e6e6e6

    .draftArticle
      margin-top 1.5em
      margin-bottom .5em

    .btn_logout
      position absolute
      bottom 2em
      left 1.5em
      width 10em

    &.hide-nav
      margin-right -13em
      transform translateX(-100%)

  #toggle-nav
    position absolute
    z-index 99999
    transition all 500ms ease-in-out 0s
    top 6em
    &.hide-nav
      left 1em
      background rgba(188, 188, 188, 0.88)

    &.show-nav
      left 12em
      color black
      &:hover
        background rgba(200, 200, 200, 0.33)

  .el-main
    color: #333
    text-align: left
    height 100% -7em
    background-color: #cbcfd3
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
