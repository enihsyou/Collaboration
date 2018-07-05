<template>
  <div id="article-layout">
    <transition name="el-fade-in">
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
    </transition>
    <div id="content-layout" @click="releaseMouse">
      <div class="inner">
        <span id="content" ref="content"></span>
        <el-button id="mod_btn" size="mini" @click.stop="modContent" v-show="callBtn">
          插入/修改
        </el-button>
      </div>
    </div>
    <div class="floatBtn" v-if="isOwner">
      <!--历史版本按钮-->
      <el-button class="btn" id="history_btn" size="large" type="info" icon="el-icon-document"
                 title="历史版本"
                 @click="listArticleHistory" circle></el-button>
      <!--编辑按钮-->
      <el-button class="btn" id="edit_btn" size="large" type="warning" icon="el-icon-edit-outline"
                 title="编辑文章信息"
                 @click="editArticleInfo" circle></el-button>
      <!--邀请按钮-->
      <el-button class="btn" id="share_btn" size="large" type="primary" icon="el-icon-share"
                 title="邀请其他小伙伴"
                 @click="shareArticle" circle></el-button>
    </div>
    <div id="editableArea" ref="editableArea" contenteditable v-show="range"></div>
    <div id="rangeArea" style="display: inline" ref="rangeArea"></div>
    <div class="cursor"></div>
  </div>
</template>

<script>
  import inviteBox from './modal_inviteBox'
  import editBox   from './modal_editBox'

  export default {
    name   : "articlePanel",
    props  : {
      id     : {
        type   : String,
        default: ''
      },
      isOwner: {
        type   : Boolean,
        default: false
      }
    },
    data() {
      const wsURL = `${this.$.protocol.includes('s') ? 'wss' : 'ws'}://${this.$.baseURL}/api/websocket`;
      const webSocketClient = Stomp.client(wsURL);
      //设置脉搏时间30s
      webSocketClient.heartbeat.outgoing = 30 * 1000;
      webSocketClient.heartbeat.incoming = 0;
      //设置消息回调
      const msgCallback = (response) => {
        // console.log('收到来自服务器的消息:', response);
      };
      //设置连接回调
      const connCallback = (frame) => {
        // console.log(`${this.$.protocol.includes('s') ? 'wss' : 'ws'}：与服务器建立连接：${frame}`);
        webSocketClient.subscribe(`/topic/pad.${this.id}`, msgCallback);
      };
      //设置连接头
      const wsHead = {
        // login: 'mylogin',
        // passcode: 'mypasscode',
        // 'client-id': 'my-client-id'
      };
      /*通过ws向服务器发送数据*/
      webSocketClient.sendMsg = (path, msg) => {
        if (webSocketClient.connected) {
          const params = {
            priority: 9
          };
          webSocketClient.send(path, params, msg);
        } else {
          throw new Error('WebSocket is not connected!')
        }
      };
      //连接
      webSocketClient.connect(wsHead, connCallback);
      return {
        workers            : [],
        current_revision   : -1,
        current_lock_id    : -1,
        title              : '',
        content            : '',
        savedContent       : '',
        range              : null,
        callBtn            : false,
        trySaveCount       : 0,
        shareToken         : null,
        shareLinkPermission: null,
        webSocketClient
      }
    },
    watch  : {
      title() {
        window.document.title = `${this.title} - NTM协同文档系统`;
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
        if (this.$refs.editableArea.innerHTML.length < 1) {
          this.$refs.editableArea.focus();
          //fixme 某些情况下会跳转到界限外的区域
        }
        //用户点击编辑框外尝试保存
        this.trySaveCount = e.target.id !== 'editableArea' ? this.trySaveCount + 1 : 0;
      },
      /*点击[插入]/[修改]后的操作*/
      modContent() {
        //保存当前range
        const selection = window.getSelection ? window.getSelection() : document.getSelection();
        if (!selection.rangeCount) return;
        const content = this.$refs.content;
        const editableArea = this.$refs.editableArea;
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
        //给文段加个临时选择标签
        let rangeArea = this.$refs.rangeArea;
        this.range.surroundContents(rangeArea);
        //获得标签的位置偏移
        const tmpContent = this.$refs.content.innerHTML;
        const rangeMatch = tmpContent.match(/<div[^>]+>(.*)<\/div>/m);
        const rawStartOffset = rangeMatch.index;
        const startOffset = tmpContent.slice(0, rawStartOffset).replace(/<br>/gm, '\n').length;
        const endOffset = startOffset + rangeMatch[1].replace(/<br>/gm, '\n').length;
        //显示载入动画
        const loading = this.$loading({
          lock      : true,
          text      : '向服务器申请修改...',
          spinner   : 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        console.log(`请求修改区间为：${startOffset}~${endOffset}`);
        //向服务器申请加锁
        this.$.ajax.post(`/pad.${this.id}.lock.acquire`, JSON.stringify({
          pad_id         : this.id,
          username       : sessionStorage.username,
          client_revision: this.current_revision,
          range          : {
            start: startOffset,
            end  : endOffset
          }
        })).then((res) => {
          this.current_lock_id = res.lock_id;
          //保存当前文本内容
          this.savedContent = this.content;
          //转化当前区域为可编辑
          this.range.surroundContents(editableArea);
          editableArea.focus();
          this.callBtn = false;
          this.trySaveCount = 0;
        }, (err) => {
          this.$message.error('申请修改失败：' + err.msg);
          this.range = null;
        }).finally(() => {
          loading.close();
        });

      },
      /*提交修改后的内容*/
      subContent() {
        //提交修改
        const loading = this.$loading({
          lock      : true,
          text      : '提交修改...',
          spinner   : 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        const editableArea = this.$refs.editableArea;
        const Reg = /<div[^>]+>(.*)<\/div>/igm;
        while (Reg.test(editableArea.innerHTML)) {
          editableArea.innerHTML = editableArea.innerHTML.replace(Reg, "$1");
        }
        console.log('提交的内容：', editableArea.innerHTML.replace(/<br>/gm, '\n'));
        this.$.ajax.post(`/pad.${this.id}.lock.release`, JSON.stringify({
          pad_id         : this.id,
          client_revision: this.current_revision,
          lock_id        : this.current_lock_id,
          modified       : true,
          replacement    : editableArea.innerHTML.replace('<br>', '\n'),
          username       : sessionStorage.username
        })).then(res => {
          this.$message.success('提交修改成功！');
          //清除编辑区
          this.clearRange();
        }, err => {
          this.$message.error('提交修改失败!');
        }).finally(_ => {
          loading.close();
        });
      },
      /*取消修改，返回到上个版本*/
      cancelSubContent() {
        this.$confirm('此操作将放弃所有的修改，恢复文档至未编辑前的状态，是否放弃编辑？', '提示', {
          confirmButtonText : '确定放弃',
          cancelButtonText  : '继续编辑',
          type              : 'error',
          showClose         : false,
          closeOnClickModal : false,
          closeOnPressEscape: false
        }).then((res) => {
          const loading = this.$loading({
            lock      : true,
            text      : '撤销修改...',
            spinner   : 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
          this.$.ajax.post(`/pad.${this.id}.lock.release`, JSON.stringify({
            pad_id         : this.id,
            client_revision: this.current_revision,
            lock_id        : this.current_lock_id,
            modified       : false,
            replacement    : '',
            username       : sessionStorage.username
          })).then(res => {
            //清除编辑区
            this.clearRange();
            this.renderContent(this.savedContent);
            this.$message.warning('已恢复编辑前的内容');
          }, err => {
            this.$message.error('撤销修改失败!');
          }).finally(_ => {
            loading.close();
          });
        }, (err) => {
          // this.$message.info('请继续编辑');
        });
      },
      // /*获得当前选区范围*/
      // getRangeOffset() {
      //   let res = {
      //     start: -1,
      //     end: -1
      //   };
      //
      //   return res;
      // },
      /*清除可编辑栏*/
      clearRange() {
        let editContentElem = this.$refs.editableArea;
        let rangeArea = this.$refs.rangeArea;
        let content = this.$refs.content.innerHTML;
        const Reg = /<div[^>]+>(.*)<\/div>/igm;
        while (Reg.test(content)) content = content.replace(Reg, "$1");
        this.renderContent(content.replace(/<br>/gm, '\n'));
        editContentElem.innerHTML = '';
        rangeArea.innerHTML = '';
        document.getElementsByTagName('body')[0].appendChild(editContentElem);
        document.getElementsByTagName('body')[0].appendChild(rangeArea);
        this.range = null;
      },
      /*从文本渲染到html页面*/
      renderContent(content) {
        if (typeof content === 'string') this.content = content;
        this.$refs.content.innerHTML = this.content.replace(/\n/gi, '<br>');
      },
      /*查看文章历史版本*/
      listArticleHistory() {
        this.$message.warning('正在施工');
      },
      /*编辑文章信息*/
      editArticleInfo() {
        const h = this.$createElement;
        this.$msgbox({
          title             : '编辑文章信息',
          message           : h(editBox, {
            props: {
              id   : this.id,
              title: this.title
            },
            on   : {
              updateTitle: (id, title) => {
                this.title = title;
                //传递修改标题的事件
                this.$emit('updateTitle', id, title)
              },
              delArticle : () => {
                this.$emit('delArticle', this.id);
              }
            }
          }),
          closeOnClickModal : false,
          closeOnPressEscape: false,
          showConfirmButton : false,
        }).then(() => {

        }, () => {

        });
      },
      /*邀请其他编辑者*/
      shareArticle() {
        const h = this.$createElement;
        this.$msgbox({
          title             : '邀请其他小伙伴',
          message           : h(inviteBox, {
            props: {
              id            : this.id,
              workers       : this.workers,
              shareToken    : this.shareToken,
              linkPermission: this.shareLinkPermission
            }
          }),
          closeOnClickModal : false,
          closeOnPressEscape: false,
          showConfirmButton : false,
        }).catch(() => {
          //关闭邀请框
        });
      },
    },
    created() {

    },
    mounted() {
      if (this.id === 'undefined') {
        //页面未加载完成的情况
        return;
      }
      //更新文章信息
      this.$.ajax.get(`/pads/${this.id}`).then((res) => {
        console.log('初始化文章数据', res);
        this.current_revision = res.pad.current_revision;
        this.content = res.pad.body;
        this.title = res.pad.title;
        this.shareToken = res.pad.invite_link.token;
        this.shareLinkPermission = res.pad.invite_link.permission;
        this.workers = res.pad.workers;
        this.workers.forEach((worker) => {
          worker.username = res.accounts[worker.account_id] || '暂无姓名_ID:' + worker.account_id;
        });
        this.renderContent();
      }, (err) => {
        this.$message.error(`初始化文档失败！${err.msg || ''}`);
        this.$router.push('/user');
      });
    },
    destroyed() {

    }
  }
</script>

<style lang="stylus" scoped>
  #editableArea
    display inline !important
    padding .1em 0 .0688em 0
    box-shadow 0 1px 0 deepskyblue
    min-width .5em

  #content-layout
    width: 794px;
    min-height: 995px;
    background-color white
    box-shadow 2px 2px .3em 2px grey
    overflow: hidden
    margin 1em 0 2em 0
    .inner
      margin 4em
      cursor text
      word-break break-all
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
    justify-content center
    align-items flex-end
    right -40px
    bottom -10vh
    width 30vw
    height 50vh
    text-align center
    transition all 100ms ease-in 100ms
    &:hover
      right 0
    .btn
      display block
      width 40px
      height 40px
      zoom 1.5
      margin .5em .5em 0 0
      &:hover
        zoom 2

  #sub_btn_group
    position absolute
    right 2em
    top .75em

    .tip
      position absolute
      margin-top 1px
      align-items center
      animation flicker 3s linear 0s infinite
      zoom 2

  @keyframes flicker
    0%
      transform translateX(-4em)
      opacity 0
    30%
      transform translateX(-2em)
      opacity 1
    50%
      transform translateX(-1.5em)
      opacity 0
    100%
      transform translateX(-1.5em)
      opacity 0


</style>
