<template>
  <div id="article-layout">
    <div id="sub_btn_group" v-show="range &&!callBtn">
      <i class="tip el-icon-d-arrow-right" v-if="trySaveCount > 2"></i>
      <el-button-group>
        <el-button id="sub_btn" type="success" size="large" @click.stop="subContent">
          提交
        </el-button>
        <el-button id="cancel_sub_btn" type="info" size="large" @click.stop="cancelSubContent">
          取消
        </el-button>
      </el-button-group>
    </div>
    <div id="content-layout" @click="releaseMouse">
      <div class="inner">
        <span id="content" ref="content"></span>
        <el-button id="mod_btn" size="mini" @click.stop="modContent" v-show="callBtn">
          插入/修改
        </el-button>
      </div>
    </div>
    <div class="floatBtn" v-if="isOwner">
      <!--编辑按钮-->
      <el-button class="btn" id="edit_btn" size="large" type="warning" icon="el-icon-document"
                 @click="editArticleInfo" circle></el-button>
      <!--邀请按钮-->
      <el-button class="btn" id="share_btn" size="large" type="primary" icon="el-icon-plus"
                 @click="shareArticle" circle></el-button>
    </div>
    <div id="editableArea" ref="editableArea" contenteditable v-show="range"></div>
    <div class="cursor"></div>
  </div>
</template>

<script>
  import inviteBox from './modal_inviteBox'
  import editBox from './modal_editBox'

  export default {
    name: "articlePanel",
    props: {
      id: {
        type: String,
        default: ''
      },
      isOwner: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        title: '',
        content: '',
        savedContent: '',
        range: null,
        callBtn: false,
        trySaveCount: 0
      }
    },
    methods: {
      /*释放鼠标动作*/
      releaseMouse(e) {
        if (this.range == null) {
          //未有编辑区域
          this.callBtn = true;
          //移动编辑按钮到鼠标处
          const mod_btn = document.querySelector('#mod_btn');
          mod_btn.innerHTML = window.getSelection().toString() === '' ? '插入' : '修改';
          mod_btn.style.left = `calc(${e.clientX}px - 2em)`;
          mod_btn.style.top = `calc(${e.clientY}px - 3em)`;
          return;
        }
        if (this.content.length < 1 || this.$refs.editableArea.innerHTML.length < 1) {
          this.$refs.editableArea.focus();
        }
        //用户点击编辑框外尝试保存
        this.trySaveCount = e.target.id !== 'editableArea' ? this.trySaveCount + 1 : 0;
      },
      /*点击插入/修改后的操作*/
      modContent() {
        //保存当前range
        const selection = window.getSelection ? window.getSelection() : document.getSelection();
        if (!selection.rangeCount) return;
        const content = this.$refs.content;
        //空文本的情况
        if (this.content.length < 1) {
          let newRange = document.createRange();
          newRange.setStart(content, 0);
          newRange.setEnd(content, 0);
          this.range = newRange;
        }
        // 从selection中获取第一个Range对象
        for (let i = 0; i < selection.rangeCount; i++) {
          const range = selection.getRangeAt(0);
          let start = range.startContainer;
          let end = range.endContainer;
          // 兼容IE11 node.contains(textNode) 永远 return false的bug
          start = start.nodeType === Node.TEXT_NODE ? start.parentNode : start;
          end = end.nodeType === Node.TEXT_NODE ? end.parentNode : end;
          if (content.contains(start) && content.contains(end)) {
            // Range对象被保存在this.range
            this.range = range;
            break
          }
        }
        //todo 向服务器申请加锁
        //保存当前文本内容
        this.savedContent = this.content;
        //转化当前区域为可编辑
        let editableArea = this.$refs.editableArea;
        // let editableArea = document.createElement('div');
        // editableArea.setAttribute('contenteditable', true);
        // editableArea.setAttribute('ref', 'editableArea');
        // editableArea.id = 'editableArea';
        this.range.surroundContents(editableArea);
        editableArea.focus();
        this.callBtn = false;
        this.trySaveCount = 0;
      },
      /*提交修改后的内容*/
      subContent() {
        //提交修改的按钮
        //todo 假装向服务器申请提交修改
        //清除编辑区
        this.clearRange();
      },
      /*取消修改，返回到上个版本*/
      cancelSubContent() {
        this.$confirm('此操作将放弃所有的修改，恢复文档至未编辑前的状态，是否放弃编辑？', '提示', {
          confirmButtonText: '确定放弃',
          cancelButtonText: '继续编辑',
          type: 'error',
          showClose: false,
          closeOnClickModal: false,
          closeOnPressEscape: false
        }).then((res) => {
          //清除编辑区
          this.clearRange();
          this.renderContent(this.savedContent);
          this.$message.info('已放弃编辑');
        }, (err) => {
          this.$message.info('请继续编辑');
        });
      },
      /*清除可编辑栏*/
      clearRange() {
        let editContentElem = this.$refs.editableArea;
        this.renderContent(this.$refs.content.innerHTML.replace(/(<div([^>]+)>)/ig, ""));
        editContentElem.innerHTML = '';
        document.getElementsByTagName('body')[0].appendChild(editContentElem);
        this.range = null;
      },
      /*编辑文章信息*/
      editArticleInfo() {
        const h = this.$createElement;
        this.$msgbox({
          title: '编辑文章信息',
          message: h(editBox, {
            props: {
              id: this.id,
              title: this.title
            },
            on: {
              updateTitle: (id, title) => {
                this.title = title;
                //传递修改标题的事件
                this.$emit('updateTitle', id, title)
              }
            }
          }),
          closeOnClickModal: false,
          closeOnPressEscape: false,
          showConfirmButton: false,
        }).then(() => {

        }, () => {

        });
      },
      /*邀请其他编辑者*/
      shareArticle() {
        const h = this.$createElement;
        this.$msgbox({
          title: '邀请其他小伙伴',
          message: h(inviteBox, {
            props: {
              id: this.id,
            }
          }),
          closeOnClickModal: false,
          closeOnPressEscape: false,
          showConfirmButton: false,
        }).catch(() => {
          //关闭邀请框
        });
      },
      /*从文本渲染到html页面*/
      renderContent(content) {
        if (typeof content === 'string') this.content = content;
        this.$refs.content.innerHTML = this.content.replace(/\n/g, '<br>');
      }
    },
    created() {

    },
    mounted() {
      if (this.id === 'undefined') {
        //页面未加载成功的情况
        return;
      }
      //更新文章信息
      this.$.ajax.get(`/pads/${this.id}`).then((res) => {
        console.log('初始化文章数据', res);
        this.content = res.body;
        this.title = res.title;
        window.document.title = `${res.title} - NTM协同文档系统`;
        this.renderContent();
      }, (err) => {
        this.$message.error(`初始化文档失败！${err.msg || ''}`);
        this.$router.push('/user');
      })
    },
    destroyed() {

    }
  }
</script>

<style lang="stylus">
  #editableArea
    display inline !important
    box-shadow 0 0 2px 2px deepskyblue
</style>

<style lang="stylus" scoped>
  #content-layout
    width: 794px;
    min-height: 995px;
    background-color white
    box-shadow 2px 2px .3em 2px grey
    cursor text
    overflow: hidden
    margin 1em 0 2em 0

    .inner
      margin 4em
      &:before
        display block
        margin-left -2.5em
        margin-top -2.5em
        content ''
        width 2em
        height 2em
        box-shadow .1em .1em 0 rgba(0, 0, 0, .3)

  #mod_btn
    position absolute
    z-index 999

  .floatBtn
    position fixed
    display flex
    flex-direction column
    justify-content space-around
    right -60px
    bottom 6em
    width 100px
    height 200px
    text-align center
    transition all 500ms ease-in 0s
    &:hover
      right 0
    .btn
      display block
      width 40px
      height 40px
      zoom 2
      margin 0

  #sub_btn_group
    position absolute
    right 2em
    top .75em
    z-index 999
    .tip
      position absolute
      margin-top 1px
      align-items center
      animation flicker 3s linear 0s infinite
      zoom 2

  @keyframes flicker
    0%
      left -4em
      opacity 0
    30%
      left -2em
      opacity 1
    50%
      left -1.5em
      opacity 0
    100%
      left -1.5em
      opacity 0


</style>
