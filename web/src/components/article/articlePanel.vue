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
    <div id="content-layout">
      <div class="inner" @click="releaseMouse">
        <span id="content" ref="content"></span>
        <el-button id="mod_btn" size="mini" @click.stop="modContent" v-show="callBtn">
          修改
        </el-button>
      </div>
    </div>
    <el-button id="share_btn" size="large" type="primary" icon="el-icon-plus"
               @click="shareArticle" circle v-if="isOwner"></el-button>
    <div id="editableArea" ref="editableArea" contenteditable v-show="range">

    </div>
    <div class="cursor"></div>
  </div>
</template>

<script>
  import $ from 'jquery'
  import inviteBox from './modal_inviteBox'

  export default {
    name: "articlePanel",
    data() {
      return {
        content: '',
        range: null,
        callBtn: false,
        resizeLayoutTimer: null,
        isOwner: true,
        trySaveCount: 0
      }
    },
    methods: {
      releaseMouse(e) {
        //未有编辑区域
        if (this.$refs.editableArea.innerHTML === '') {
          this.callBtn = true;
          //移动编辑按钮到鼠标处
          const mod_btn = document.querySelector('#mod_btn');
          mod_btn.innerHTML = window.getSelection().toString() === '' ? '插入' : '修改';
          mod_btn.style.left = `calc(${e.clientX}px - 2em)`;
          mod_btn.style.top = `calc(${e.clientY}px - 3em)`;
          return;
        }
        //用户点击编辑框外尝试保存
        this.trySaveCount = e.target.id !== 'editableArea' ? this.trySaveCount + 1 : 0;

      },
      subContent() {
        //提交修改的按钮
        console.log('假装向服务器申请提交修改');
        this.range.insertNode('测试啊');

        this.range.insertNode(this.range.cloneContents());
        this.range = null;
      },
      cancelSubContent() {
        console.log(this.range);
        //取消修改的按钮
        console.log('假装向服务器申请提交修改');
        let lastContent = this.$refs.content.innerHTML;
        $(`<span>${$('#editableArea').html()}</span>`).replaceAll('#editableArea');
        this.$refs.content.innerHTML = lastContent.replace(/(<div([^>]+)>)/ig, "");
        this.range = null;
      },
      modContent() {
        //保存当前range
        const selection = window.getSelection ? window.getSelection() : document.getSelection();
        if (!selection.rangeCount) {
          return
        }
        const content = this.$refs.content;
        for (let i = 0; i < selection.rangeCount; i++) {
          // 从selection中获取第一个Range对象
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
        //转化当前区域为可编辑
        console.log('假装向服务器请求是否可以加锁然后还通过了');
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
      shareArticle() {
        const h = this.$createElement;
        this.$msgbox({
          title: '邀请其他小伙伴',
          message: h(inviteBox, {
            id: 'hex_string'
          }),
          closeOnClickModal: false,
          closeOnPressEscape: false,
          showConfirmButton: false,
        }).catch(() => {
          //关闭邀请框
        });
      }
    },
    created() {
    },
    mounted() {
      console.log(this.$refs.content.__proto__);
      //todo 根据文章id获取文章content
      //。。。

//       this.content =
//         `　　我与父亲不相见已二年余了，我最不能忘记的是他的背影。那年冬天，祖母死了，父亲的差使也交卸了，正是祸不单行的日子，我从北京到徐州，打算跟着父亲奔丧回家。到徐州见着父亲，看见满院狼藉的东西，又想起祖母，不禁簌簌地流下眼泪。父亲说，“事已如此，不必难过，好在天无绝人之路！”
// 　　回家变卖典质，父亲还了亏空；又借钱办了丧事。这些日子，家中光景很是惨淡，一半为了丧事，一半为了父亲赋闲。丧事完毕，父亲要到南京谋事，我也要回北京念书，我们便同行。
// 　　到南京时，有朋友约去游逛，勾留了一日；第二日上午便须渡江到浦口，下午上车北去。父亲因为事忙，本已说定不送我，叫旅馆里一个熟识的茶房陪我同去。他再三嘱咐茶房，甚是仔细。但他终于不放心，怕茶房不妥帖；颇踌躇了一会。其实我那年已二十岁，北京已来往过两三次，是没有甚么要紧的了。他踌躇了一会，终于决定还是自己送我去。我两三回劝他不必去；他只说，“不要紧，他们去不好！”
// 　　我们过了江，进了车站。我买票，他忙着照看行李。行李太多了，得向脚夫行些小费，才可过去。他便又忙着和他们讲价钱。我那时真是聪明过分，总觉他说话不大漂亮，非自己插嘴不可。但他终于讲定了价钱；就送我上车。他给我拣定了靠车门的一张椅子；我将他给我做的紫毛大衣铺好坐位。他嘱我路上小心，夜里警醒些，不要受凉。又嘱托茶房好好照应我。我心里暗笑他的迂；他们只认得钱，托他们直是白托！而且我这样大年纪的人，难道还不能料理自己么？唉，我现在想想，那时真是太聪明了！
// 　　我说道，“爸爸，你走吧。”他望车外看了看，说，“我买几个橘子去。你就在此地，不要走动。”我看那边月台的栅栏外有几个卖东西的等着顾客。走到那边月台，须穿过铁道，须跳下去又爬上去。父亲是一个胖子，走过去自然要费事些。我本来要去的，他不肯，只好让他去。我看见他戴着黑布小帽，穿着黑布大马褂，深青布棉袍，蹒跚地走到铁道边，慢慢探身下去，尚不大难。可是他穿过铁道，要爬上那边月台，就不容易了。他用两手攀着上面，两脚再向上缩；他肥胖的身子向左微倾，显出努力的样子。这时我看见他的背影，我的泪很快地流下来了。我赶紧拭干了泪，怕他看见，也怕别人看见。我再向外看时，他已抱了朱红的橘子望回走了。过铁道时，他先将橘子散放在地上，自己慢慢爬下，再抱起橘子走。到这边时，我赶紧去搀他。他和我走到车上，将橘子一股脑儿放在我的皮大衣上。于是扑扑衣上的泥土，心里很轻松似的，过一会说，“我走了；到那边来信！”我望着他走出去。他走了几步，回过头看见我，说，“进去吧，里边没人。”等他的背影混入来来往往的人里，再找不着了，我便进来坐下，我的眼泪又来了。
// 　　近几年来，父亲和我都是东奔西走，家中光景是一日不如一日。他少年出外谋生，独力支持，做了许多大事。那知老境却如此颓唐！他触目伤怀，自然情不能自已。情郁于中，自然要发之于外；家庭琐屑便往往触他之怒。他待我渐渐不同往日。但最近两年的不见，他终于忘却我的不好，只是惦记着我，惦记着我的儿子。我北来后，他写了一信给我，信中说道，“我身体平安，惟膀子疼痛利害，举箸提笔，诸多不便，大约大去之期不远矣。”我读到此处，在晶莹的泪光中，又看见那肥胖的，青布棉袍，黑布马褂的背影。唉！我不知何时再能与他相见！`;
      this.content = (_ => {
        let res = '';
        const count = Math.random() * 500 + 100;
        for (let i = 0; i < count; i++)
          res += this.$route.params.id + '\t';
        return res.toString();
      })();
      this.$refs.content.innerHTML = this.content.replace(/\n/g, '<br>');
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
    .inner
      margin 1em 0 2em 0
      padding 4em

  #mod_btn
    position absolute
    z-index 999

  #share_btn
    position absolute
    right 1em
    bottom 3em
    zoom 2

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