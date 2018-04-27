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
            <router-link to="/forgot">忘记密码</router-link>
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
          this.$Message.error('账户名不能为空');
          return;
        }
        if (this.$.isEmpty(this.password)) {
          this.$Message.error('密码不能为空');
          return;
        }
        if (this.isSent) return;
        this.$.ajax.post('/user/login', JSON.stringify({
          username: this.username,
          password: this.password
        })).then((res) => {
            if (!res.headers['x-auth-token']) {
              this.$Message.error(`登录失败：${res.message}`);
              return;
            }
            if (res.data.role === 'ROOT') {
              this.$Message.error(`ROOT用户禁止登陆，请使用内部接口操作`);
              return;
            }
            delete sessionStorage.students;
            delete sessionStorage.sid;
            delete sessionStorage.secret;
            delete localStorage.selectedCourse;
            sessionStorage.token = res.headers['x-auth-token'];
            sessionStorage.username = res.data.username;
            sessionStorage.nickname = res.data.nickname;
            sessionStorage.role = res.data.role;
            sessionStorage.school_id = res.data.school_id;
            sessionStorage.school_name = res.data.school_name;
            sessionStorage.last_login_time = res.data.last_login_time;
            this.$Message.success('登录成功');
            this.$router.push(this.$route.query.redirect || '/user');
          }, (res) => {
            this.$Message.error(`登录失败：${res.message}`);
          }
        ).finally(() => {
            msg();
            this.isSent = false;
          }
        );
        const msg = this.$Message.loading({
          content: '登录中...',
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
