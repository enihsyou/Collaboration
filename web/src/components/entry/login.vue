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
      <h1>NTM协同文档</h1>
      <div class="styled-input">
        <input type="text" class="styled-input__input" title="" v-model="username" maxlength="20"
               @focus="placeholderAnimationIn" @blur="placeholderAnimationOut">
        <div class="styled-input__placeholder">
          <span class="styled-input__placeholder-text">
              <span class="letter" v-for="char in '邮箱 / 用户名'">{{char}}</span>
          </span>
        </div>
        <div class="styled-input__circle"></div>
      </div>
      <div class="styled-input">
        <input type="password" class="styled-input__input" title="" v-model="password" maxlength="16"
               @focus="placeholderAnimationIn" @blur="placeholderAnimationOut">
        <div class="styled-input__placeholder">
          <span class="styled-input__placeholder-text">
              <span class="letter" v-for="char in '密码'">{{char}}</span>
          </span>
        </div>
        <div class="styled-input__circle"></div>
      </div>
      <button type="button" class="styled-button" @click="submit">
        <span class="styled-button__real-text-holder">
          <span class="styled-button__real-text">登 录</span>
          <span class="styled-button__moving-block face">
            <span class="styled-button__text-holder">
                <span class="styled-button__text">登 录</span>
            </span>
          </span>
          <span class="styled-button__moving-block back">
            <span class="styled-button__text-holder">
                <span class="styled-button__text">登 录</span>
            </span>
          </span>
        </span>
      </button>
      <router-link to="/register">注册新用户</router-link>
      <!--<router-link to="/forgot">忘记密码</router-link>-->
    </div>
  </div>
</template>

<script>

  export default {
    name: "signin",
    data() {
      return {
        username: '',
        password: '',
        isSent: false,
        isDev: this.$.env === 'development'
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
          this.$message.error('账户名不能为空');
          return;
        }
        if (this.$.isEmpty(this.password)) {
          this.$message.error('密码不能为空');
          return;
        }
        const loading = this.$loading({
          lock: true,
          text: '登录中...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.5)'
        });
        this.$.ajax.post('/account/login', JSON.stringify({
          username: this.username,
          password: this.password
        })).then((res) => {
            this.$message.success('登录成功');
            this.$router.push(this.$route.query.redirect || '/user');
          }, (err) => {
            this.$message.error(`登录失败：${err.msg}`);
          }
        ).finally(() => {
            loading.close();
          }
        );
      }
    },
    created() {
      delete sessionStorage.token;
    }
  }
</script>

<style scoped>

</style>
