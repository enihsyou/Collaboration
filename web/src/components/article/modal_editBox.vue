<template>
  <div class="container">
    <div class="edit-item">
      <p class="title">修改文章标题：</p>
      <el-input v-model="title">
        <el-button slot="append" size="small" icon="el-icon-edit"
                   @click="modTitle">提交修改
        </el-button>
      </el-input>
    </div>
    <div class="edit-item">
      <el-button class="delete-btn" type="danger" @click="delArticle">
        删除文章
      </el-button>
    </div>
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
        innerTitle: this.title
      }
    },
    methods: {
      modTitle() {
        this.$emit('updateTitle', this.id, this.title)
      },
      delArticle() {

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
