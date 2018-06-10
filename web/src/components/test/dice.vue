<template>
  <div style="width: 30em;margin: 2em;text-align: center">
    <div style="margin: 2em">
      <el-button @keypress.enter="roll" @click="roll">摇一个！</el-button>
    </div>
    <div style="text-align: left">
      <div>历史记录：</div>
      <el-tag style="margin: .5em" :type="randomCokor(num)" v-for="num in nums" :key="num">
        {{num}}
      </el-tag>
    </div>
  </div>
</template>

<script>
  export default {
    name: "dice",
    data() {
      return {
        nums: []
      }
    },
    methods: {
      roll() {
        this.nums.push(Math.ceil(Math.random() * 6));
      },
      randomCokor(num) {
        return ['', 'success', 'info', 'warning', 'danger'][num % 5];
      }
    }
  }
</script>

<style scoped>

</style>
