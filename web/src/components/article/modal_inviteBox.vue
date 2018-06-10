<template>
  <div class="container">
    <div class="invite-item initPermission">
      <p class="title">设置分享链接权限：</p>
      <el-select style="width:100%;" v-model="linkPermission" placeholder="请选择">
        <el-option label="通过链接获得文档的任何人【只可查看】" value="CAN_VIEW">
          <i class=" el-icon-search"></i>
          <span class="select-option-label">通过链接获得文档的任何人【只可查看】</span>
        </el-option>
        <el-option label="通过链接获得文档的任何人【可编辑】" value="CAN_EDIT">
          <i class=" el-icon-edit"></i>
          <span class="select-option-label">通过链接获得文档的任何人【可编辑】</span>
        </el-option>
        <el-option label="仅以下列表成员【可查看/编辑】" value="groups">
          <i class=" el-icon-edit-outline"></i>
          <span class="select-option-label">仅以下列表成员【可查看/编辑】</span>
        </el-option>
        <el-option label="仅自己【可查看/编辑】" value="self">
          <i class=" el-icon-circle-close-outline"></i>
          <span class="select-option-label">仅自己【可查看/编辑】</span>
        </el-option>
      </el-select>
    </div>
    <div class="invite-item inviteLink">
      <p class="title">通过链接邀请：</p>
      <el-tooltip class="item" effect="dark" content="复制到剪贴板" placement="right">
        <el-input ref="shareLink" v-model="inviteLink" placeholder="分享链接生成中..." readonly>
          <el-button slot="append" size="small" icon="el-icon-document"
                     @click="copyLink"></el-button>
        </el-input>
      </el-tooltip>
    </div>
    <div class="invite-item invitedGroup">
      <p class="title">所有成员：</p>
      <div class="groupMemberItem" v-for="person in workers" :key="person.id">
        <div class="groupList">
          <span>
            {{person.username.username}}
            <span class="right" style="width: 8em;text-align: center"
                  v-if="person.share_level==='OWN'">
              【拥有者】
            </span>
            <el-select v-model="person.share_level" class="right"
                       style="width: 8em" v-else>
              <el-option label="可查看" value="CAN_VIEW">
                <i class=" el-icon-search"></i>
                <span class="select-option-label">可查看</span>
              </el-option>
              <el-option label="可编辑" value="editable">
                <i class=" el-icon-edit"></i>
                <span class="select-option-label">可编辑</span>
              </el-option>
            </el-select>
          </span>
        </div>
      </div>
    </div>
    <div class="invite-item inviteMember">
      <p class="title">邀请成员成员：</p>
      <el-autocomplete style="width:100%" class="inline-input" v-model="inviteMemberEmail" placeholder="请输入对方邮箱/用户名..."
                       :trigger-on-focus="false" :fetch-suggestions="querySearch" @select="addMember">
        <el-button slot="append" icon="el-icon-search">搜索</el-button>
      </el-autocomplete>
    </div>
  </div>
</template>

<script>
  export default {
    name: "modal_inviteBox",
    props: {
      id: {
        type: String,
        default: ''
      },
      workers: {
        type: Array,
        default: [
          // {
          //   "account_id": 1,
          //   "share_level": "CAN_VIEW",
          //   "share_time": "2018-05-21T21:30:53.405"
          // },
          // {
          //   "account_id": 8,
          //   "share_level": "OWN",
          //   "share_time": "2018-05-21T21:30:53.405"
          // }
        ]
      }
    },
    data() {
      return {
        linkPermission: 'CAN_VIEW',
        inviteLink: '',
        inviteMemberEmail: '',
      }
    },
    methods: {
      copyLink() {
        if (this.inviteLink.length < 1) {
          this.$message.error('复制到剪贴板失败，请等待邀请链接生成完成。');
          return;
        }
        if (document.execCommand) {
          this.$refs.shareLink.select();
          document.execCommand("copy");
          this.$message.success('复制到剪贴板成功。')
        } else {
          this.$message.error('复制到剪贴板失败，请自行选择复制。')
        }
      },
      querySearch(queryString, expendList) {
        const res = [];
        //todo 去服务器搜啊
        // 调用 callback 返回建议列表的数据
        console.log('搜到数据：', res);
        expendList(res);
      },
      addMember(member) {
        this.inviteMemberEmail = '';
        console.log('添加新成员：', member);
      }
    }
  }
</script>

<style lang="stylus" scoped>
  .container
    overflow auto

  .invite-item
    font-weight bold
    margin 0 .5em 1em .5em
    .title
      margin 0 0 .5em .5em

  .select-option-label
    padding-left .2em

  .invitedGroup
    .groupMemberItem
      display inline-block
      width 100%
      line-height 3em
      .groupList
        margin 0 1em 0 1em

</style>
