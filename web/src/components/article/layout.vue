<template>
  <el-container>
    <!--header-->
    <el-header height="4em">
      <img class="left logo" src="../../assets/img/img_logo.png" alt="" draggable="false">
      <h1 class="title untouchable">NTM协同文档</h1>
    </el-header>
    <!--body-->
    <el-container>
      <!--nav-->
      <el-aside :class="{'show-nav':!navHasHide,'hide-nav':navHasHide}" width="13em">
        <!--draftArticle-->
        <el-button class="draftArticle center" size="large" type="primary" icon="el-icon-plus"
                   @click="draftArticle">
          新建文章
        </el-button>
        <el-menu :default-openeds="['myArticles','otherArticles']"
                 :default-active="currentActiveItem">
          <!--我的文章-->
          <el-submenu class="submenu" index="myArticles">
            <template slot="title">
              <i class="el-icon-document"></i>
              <span>我的文章</span>
            </template>
            <el-menu-item index="null" disabled v-if="myArticles.length===0"> —— 暂无文章 ——</el-menu-item>
            <el-menu-item class="submenu-item" v-for="paper in myArticles" :key="paper.id" :index="String(paper.id)"
                          @click="changeArticle(paper)"
                          :title="paper.title">
              {{paper.title}}
            </el-menu-item>
          </el-submenu>
          <!--我参与的文章-->
          <el-submenu class="submenu" index="otherArticles">
            <template slot="title">
              <i class="el-icon-tickets"></i>
              <span>我参与的文章</span>
            </template>
            <el-menu-item index="null" disabled v-if="otherArticles.length===0"> —— 暂无文章 ——</el-menu-item>
            <el-menu-item class="submenu-item" v-for="paper in otherArticles" :key="paper.id" :index="String(paper.id)"
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
        <!--默认显示的内容-->
        <div v-if="currentArticle.id == null">
          <p class="placeholder untouchable">← 点击左方文件柜，开始你的协同文档之旅~</p>
        </div>
        <router-view :id="String(currentArticle.id)"
                     :isOwner="currentArticle.share_level==='OWN'"
                     :key="currentArticle.id"
                     @updateTitle="updateTitle"
                     @delArticle="delArticle"
                     v-else/>
      </el-main>
      <!--hideNavBtn-->
      <el-button id="toggle-nav" :class="{'show-nav':!navHasHide,'hide-nav':navHasHide}"
                 :icon="navHasHide?'el-icon-arrow-right':'el-icon-arrow-left'"
                 type="text" @click="toggleNav" circle>
      </el-button>
    </el-container>
    <!--footer-->
    <el-footer :class="{'untouchable':true,'show-nav':!navHasHide,'hide-nav':navHasHide}" height="3em">
      Design By Sleaf
    </el-footer>
  </el-container>
</template>

<script>
  class Article {
    constructor(id, title, level) {
      this.id = id;
      this.title = title;
      this.share_level = level;
    }
  }

  export default {
    name: "layout",
    data() {
      return {
        navHasHide: false,
        myArticles: [],
        otherArticles: [],
        currentArticle: {}
      }
    },
    computed: {
      currentActiveItem() {
        return this.$route.params.id;
      }
    },
    methods: {
      /*起草新文章*/
      draftArticle() {
        this.$prompt('请输入文章标题', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.{1,20}/,
          inputErrorMessage: '标题字数应在1~20字之间！'
        }).then((dialogInfo) => {
          /*载入动画*/
          const loading = this.$loading({
            lock: true,
            text: '新建文档中...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
          /*用户输入标题后发送给服务器*/
          this.$.ajax.post('/pads', JSON.stringify({
            title: dialogInfo.value
          })).then((res) => {
            this.currentArticle = new Article(res.id, dialogInfo.value, 'OWN');
            this.myArticles.push(this.currentArticle);
            this.hideNav();
            this.$router.push(`/user/${res.id}`)
          }, (err) => {
            this.$message.error('新建失败！' + err.msg);
          }).finally(() => {
            loading.close();
          });
        }, () => {
        });
      },
      /*切换文章页*/
      changeArticle(currentArticle) {
        this.currentArticle = currentArticle;
        this.hideNav();
        this.$router.push(`/user/${this.currentArticle.id}`);
        console.log(`跳转到文章：${this.currentArticle.id}-《${this.currentArticle.title}》`);
      },
      /*更新了标题*/
      updateTitle(id, newTitle) {
        const callback = (article, index, newTitle) => {
          const newArticle = new Article(article.id, newTitle, article.share_level);
          if (index < this.myArticles.length) {
            //在我的文章里
            this.myArticles.splice(index, 1, newArticle)
          } else {
            //在其他文章里
            this.otherArticles.splice(index - this.myArticles.length, 1, newArticle)
          }
        };
        this.execArticle(id, callback, newTitle)
      },
      /*删除了文章*/
      delArticle(id) {
        this.$msgbox.close();
        this.currentArticle = {};
        const callback = (article, index) => {
          if (index < this.myArticles.length) {
            //在我的文章里
            this.myArticles.splice(index, 1)
          } else {
            //在其他文章里
            this.otherArticles.splice(index - this.myArticles.length, 1)
          }
        };
        this.execArticle(id, callback);
        this.showNav();
        this.$router.push('/user');
      },
      /*查找文章并执行动作*/
      execArticle(id, callback, ...args) {
        for (let [index, article] of [...this.myArticles, ...this.otherArticles].entries()) {
          if (String(article.id) === String(id)) {
            if (callback) callback(article, index, ...args);
            return true;
          }
        }
        return false;
      },
      /*隐藏了导航栏*/
      hideNav() {
        this.navHasHide = true;
      },
      /*显示导航栏*/
      showNav() {
        this.navHasHide = false;
      },
      /*切换显示导航栏*/
      toggleNav() {
        this.navHasHide = !this.navHasHide;
      },
      /*退出登录*/
      logout() {
        delete sessionStorage.token;
        this.$message.success('登出成功');
        this.$router.push('/login');
      }
    },
    created() {
      const loading = this.$loading({
        lock: true,
        text: '数据初始化...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      /*从服务器获取账户内文章*/
      this.$.ajax.get('/pads?level=brief').then((res) => {
        console.log('初始化文章列表:', res);
        let articleID = this.$route.params.id;
        for (let pad of res.pads) {
          /*文章分类*/
          if (pad.share_level === 'OWN') {
            this.myArticles.push(pad);
          } else {
            this.otherArticles.push(pad)
          }
          /*确认当前文章*/
          if (articleID === String(pad.id)) {
            this.hideNav();
            this.currentArticle = pad;
          }
        }
        /*存在id但未找到*/
        if (articleID && !this.currentArticle.id) {
          this.$router.push('/user');
        }
        /*排个序*/
        this.myArticles.sort((a, b) => a.id > b.id);
        this.otherArticles.sort((a, b) => a.id > b.id);
      }, (err) => {
        const h = this.$createElement;
        this.$alert(`${err.msg || ''}`, '初始化失败', {
          center: true,
          showClose: this.$.env === 'development',
          showCancelButton: false,
          confirmButtonText: '返回登录',
          closeOnClickModal: false,
          closeOnPressEscape: false
        }).then((res) => {
          this.$router.push('/login');
        }, (err) => {
          this.$message.warning('哼，居然是开发者');
        });
      }).finally(() => {
        loading.close();
      })
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
    box-shadow 0 -1px 4px #909090 inset
    overflow hidden
    .logo
      height 3em
      margin .5em
      border-radius 25%
      transition transform 200ms ease-in-out 0s
      &:hover
        transform rotate(-10deg)

  .el-aside
    position relative
    color: #333
    text-align: left
    background-color: white
    overflow-x hidden
    transition all 500ms ease-in-out 0s
    box-shadow 1px 0 1px #757575 inset
    background linear-gradient(300deg, white, rgba(155, 180, 200, .33))
    .el-menu
      background linear-gradient(-90deg, white 80%, rgba(155, 180, 200, .1))
    .submenu-item
      background rgba(155, 180, 200, .2)
      box-shadow 0 1px 0 lightgray
      white-space nowrap
      text-overflow ellipsis
      overflow hidden
      &:hover
        zoom 1.2
        background rgba(160, 200, 255, .33)
      &:nth-child(1)
        border-top 1px solid lightgray

    .draftArticle
      margin-top 1.5em
      margin-bottom 1.5em

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
    transition all 500ms ease-in-out 0s
    .placeholder
      color darkgray
      font-size 2em
      transform translateY(20vh)

  .el-footer
    background-color: white
    color: #333
    box-shadow 0 1px 0 lightgray inset
    text-align center
    line-height: 3em
    transition all 500ms ease-in-out 0s
    background linear-gradient(60deg, white, rgba(155, 180, 200, .2))
    &.hide-nav
      margin-bottom -3em
      transform translateY(100%)
</style>
