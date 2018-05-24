<template>
  <div class="container">
    <div class="edit-item">
      <p class="title">修改文章标题：</p>
      <el-input v-model="title">
        <el-button slot="append" size="small" icon="el-icon-edit"
                   :loading="editBtnLoading"
                   @click="modTitle">提交修改
        </el-button>
      </el-input>
    </div>
    <div class="edit-item">
      <el-button class="delete-btn" type="danger"
                 :loading="deleteBtnLoading"
                 @click="showDelArticleDialog">
        删除文章
      </el-button>
    </div>
    <el-dialog title="提示" :visible.sync="showDelDialog"
               :close-on-click-modal="false" :show-close="false"
               append-to-body>
      删除文章后所有用户将不能编辑，此操作不可恢复，是否继续？
      <div slot="footer">
        <el-button type="info" :disabled="deleteBtnLoading" @click="showDelDialog=false" plain>取消</el-button>
        <el-button type="danger" :loading="deleteBtnLoading" @click="delArticle">确定删除该文章</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "modal_editBox",
    props: {
      id: {
        type: String,
        default: ''
      },
      title: {
        type: String,
        default: ''
      }
    },
    data() {
      return {
        showDelDialog: false,
        editBtnLoading: false,
        deleteBtnLoading: false,
      }
    },
    methods: {
      modTitle() {
        this.editBtnLoading = true;
        this.$.ajax.put(`/pads/${this.id}`, JSON.stringify({
          title: this.title
        })).then((res) => {
          this.$message.success(`修改文章标题成功`);
          this.$emit('updateTitle', this.id, this.title)
        }, (err) => {
          this.$message.error(`修改文章标题失败：${err.msg}`);
        }).finally(() => {
          this.editBtnLoading = false;
        })
      },
      showDelArticleDialog() {
        this.showDelDialog = true;
      },
      delArticle() {
        this.deleteBtnLoading = true;
        this.$.ajax.delete(`/pads/${this.id}`).then((res) => {
          this.showDelDialog = false;
          this.$message.success('删除文章成功');
          this.$emit('delArticle');
        }, (err) => {
          this.$message.error('删除文章失败:' + err.msg);
        }).finally(() => {
          this.deleteBtnLoading = false;
        })
      }
    },
    mounted() {
    },
  }
</script>

<style lang="stylus" scoped>
  .edit-item
    font-weight bold
    margin 0 .5em 1em .5em
    .title
      margin 0 0 .5em .5em

  .delete-btn
    width 80%
    text-align center
    margin 2em 10% 0 10%
</style>
