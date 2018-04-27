<template>
  <div class="container">
    <div class="invite-item initPermission">
      <p class="title">设置分享链接权限：</p>
      <el-select style="width:100%;" v-model="linkPermission" placeholder="请选择">
        <el-option label="通过链接获得文档的任何人【只可查看】" value="readonly">
          <i class=" el-icon-search"></i>
          <span class="select-option-label">获得文档的任何人【只可查看】</span>
        </el-option>
        <el-option label="通过链接获得文档的任何人【可编辑】" value="editable">
          <i class=" el-icon-edit"></i>
          <span class="select-option-label">获得文档的任何人【可编辑】</span>
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
        <el-input v-model="inviteLink" readonly>
          <el-button slot="append" size="small" icon="el-icon-document"
                     @click="copyLink"></el-button>
        </el-input>
      </el-tooltip>
    </div>
    <div class="invite-item invitedGroup">
      <p class="title"> 所有成员： </p>
      <span class="groupMemberItem" v-for="person in invitedGroup" :key="person.id">
        <div class="groupList">
          <span>
            {{person.name}}
            <el-select v-model="person.permission" class="right"
                       style="width: 8em">
              <el-option label="可查看" value="readonly">
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
      </span>
    </div>
    <div class="invite-item inviteMember">
      <p class="title">邀请成员成员：</p>
      <el-autocomplete style="width:100%" class="inline-input" v-model="inviteMemberName" placeholder="请输入对方邮箱..."
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
      }
    },
    data() {
      return {
        linkPermission: 'readonly',
        inviteLink: 'https://doc.ntm.com/invite/xxxxxxxxx',
        invitedGroup: [
          {
            id: 'qweqasds',
            name: '李狗蛋',
            permission: 'readonly'
          },
          {
            id: 'ewrewr',
            name: '二闹子',
            permission: 'editable'
          },
        ],
        inviteMemberName: '',

      }
    },
    methods: {
      copyLink() {
        console.log('copyLink!');
      },
      querySearch(queryString, expendList) {
        const res = [];
        //todo 去服务器搜啊
        // 调用 callback 返回建议列表的数据
        console.log('搜到数据：', res);
        expendList(res);
      },
      addMember(member) {
        this.inviteMemberName = '';
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
      line-height 3em
      .groupList
        margin 0 1em 0 1em

</style>
