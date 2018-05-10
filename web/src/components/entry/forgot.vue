<template>
    <div class="form">
        <div class="form__cover"></div>
        <div class="form__loader">
            <div class="spinner active">
                <svg class="spinner__circular" viewBox="25 25 50 50">
                    <circle class="spinner__path" cx="50" cy="50" r="20" fill="none" stroke-width="4"
                            stroke-miterlimit="10">
                    </circle>
                </svg>
            </div>
        </div>
        <div class="form__content" @keypress.enter="submit">
            <h1>找回密码</h1>
            <div class="styled-input">
                <input type="text" class="styled-input__input" title="" v-model="username" maxlength="20"
                       @focus="placeholderAnimationIn" @blur="placeholderAnimationOut">
                <div class="styled-input__placeholder">
                        <span class="styled-input__placeholder-text">
                            <span class="letter" v-for="char in '注册邮箱'">{{char}}</span>
                        </span>
                </div>
                <div class="styled-input__circle"></div>
            </div>
            <button type="button" class="styled-button" @click="submit">
                    <span class="styled-button__real-text-holder">
                        <span class="styled-button__real-text">找回密码</span>
                        <span class="styled-button__moving-block face">
                            <span class="styled-button__text-holder">
                                <span class="styled-button__text">找回密码</span>
                            </span>
                        </span>
                        <span class="styled-button__moving-block back">
                            <span class="styled-button__text-holder">
                                <span class="styled-button__text">找回密码</span>
                            </span>
                        </span>
                    </span>
            </button>
            <router-link to="/login">返回登录</router-link>
        </div>
    </div>
</template>

<script>

  export default {
    name: "forgot",
    data() {
      return {
        username: '',
        isSent: false
      }
    },
    methods: {
      placeholderAnimationIn(e) {
        this.$emit('placeholderAnimationIn', e);
      },
      placeholderAnimationOut(e) {
        this.$emit('placeholderAnimationOut', e);
      },
      submit() {
        if (this.$.isEmpty(this.username)) {
          this.$Message.error('注册邮箱不能为空');
          return;
        }
        if (this.isSent) return;
        this.$.ajax.post(`/user/forgot?username=${this.username}`).then((res) => {
            this.$Modal.success({
              title: '发送重置密码请求成功',
              content: '请查看注册邮箱，使用邮件中的重置链接进行密码重置',
              closable: false
            });
          }, (res) => {
            this.$Message.error(`发送请求失败：${res.message}`);
          }
        ).finally(() => {
            msg();
            this.isSent = false;
          }
        );
        const msg = this.$Message.loading({
          content: '发送请求中...',
          duration: 0
        });
        this.isSent = true;
      }
    },
    created() {
      delete sessionStorage.token;
    }
  }
</script>

<style scoped>

</style>
