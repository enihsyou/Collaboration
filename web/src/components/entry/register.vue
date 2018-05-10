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
            <h1>新用户注册</h1>
            <div class="overtime" v-if="isAvailable||username===''||token===''">
                该链接已失效，请重新获取！
            </div>
            <div v-else>
                <div class="styled-input filled">
                    <input type="text" class="styled-input__input" title="" v-model="username" maxlength="20" disabled>
                    <div class="styled-input__placeholder">
                        <span class="styled-input__placeholder-text">
                            <span class="letter active" v-for="char in '注册邮箱'">{{char}}</span>
                        </span>
                    </div>
                    <div class="styled-input__circle"></div>
                </div>
                <div class="styled-input">
                    <input type="text" class="styled-input__input" title="" v-model="nickname" maxlength="20"
                           @focus="placeholderAnimationIn" @blur="placeholderAnimationOut">
                    <div class="styled-input__placeholder">
                        <span class="styled-input__placeholder-text">
                            <span class="letter" v-for="char in '昵称'">{{char}}</span>
                        </span>
                    </div>
                    <div class="styled-input__circle"></div>
                </div>
                <div class="styled-input">
                    <input type="password" class="styled-input__input" title="" v-model="password"
                           @focus="placeholderAnimationIn" @blur="placeholderAnimationOut">
                    <div class="styled-input__placeholder">
                        <span class="styled-input__placeholder-text">
                            <span class="letter" v-for="char in '密码'">{{char}}</span>
                        </span>
                    </div>
                    <div class="styled-input__circle"></div>
                </div>
                <div class="styled-input">
                    <input type="password" name="confirmPassword" class="styled-input__input" title=""
                           v-model="confirmPassword"
                           @focus="placeholderAnimationIn" @blur="placeholderAnimationOut">
                    <div class="styled-input__placeholder">
                        <span class="styled-input__placeholder-text">
                            <span class="letter" v-for="char in '确认密码'">{{char}}</span>
                        </span>
                    </div>
                    <div class="styled-input__circle"></div>
                </div>
                <button type="button" class="styled-button" @click="submit">
                    <span class="styled-button__real-text-holder">
                        <span class="styled-button__real-text">注 册</span>
                        <span class="styled-button__moving-block face">
                            <span class="styled-button__text-holder">
                                <span class="styled-button__text">注 册</span>
                            </span>
                        </span>
                        <span class="styled-button__moving-block back">
                            <span class="styled-button__text-holder">
                                <span class="styled-button__text">注 册</span>
                            </span>
                        </span>
                    </span>
                </button>
                <router-link to="/login">返回登录</router-link>
            </div>
        </div>
    </div>
</template>

<script>
  export default {
    name: "signup",
    data() {
      return {
        username: '',
        token: this.$route.query.token,
        isAvailable: false,
        nickname: '',
        password: '',
        confirmPassword: '',
        isSent: false,
        isDup: false
      }
    },
    methods: {
      placeholderAnimationIn(e) {
        this.$emit('placeholderAnimationIn', e);
      },
      placeholderAnimationOut(e) {
        this.$emit('placeholderAnimationOut', e);
        switch (e.target.name) {
          case 'confirmPassword':
            if (this.$.isEmpty(this.password, this.confirmPassword)) {
              this.$Message.error('请完整填写密码信息');
              return;
            }
            if (this.password !== this.confirmPassword) {
              this.$Message.error('两次输入的密码不一致');
              return;
            }
            break;
          default:
        }
      },
      submit() {
        if (this.$.isEmpty(this.password, this.confirmPassword)) {
          this.$Message.error('请完整填写密码信息');
          return;
        }
        if (this.password !== this.confirmPassword) {
          this.$Message.error('两次输入的密码不一致');
          return;
        }
        if (this.password.length < 6 || this.password.length > 16) {
          this.$Message.error('密码长度应大于6位且小于16位！');
          return;
        }
        if (this.isSent) return;
        this.$.ajax.post(`/user/register?token=${this.token}`, JSON.stringify({
          password: this.password,
          nickname: this.nickname
        })).then((res) => {
            this.$Message.success('注册成功，请登录');
            this.$router.replace('/login');
          }, (res) => {
            this.$Message.error(`注册失败：${res.message}`);
          }
        ).finally(() => {
            msg();
            this.isSent = false;
          }
        );
        const msg = this.$Message.loading({
          content: '注册中...',
          duration: 0
        });
        this.isSent = true;
      }
    },
    created() {
      this.$.ajax.get(`/token/register?token=${this.token}`).then((res) => {
        console.log(res);
        this.username = res.data.invitee;
        this.isAvailable = res.data.is_expired || res.data.is_used;
      }, (errRes) => {
        this.$Message.error('初始化失败，请联系管理员');
        this.isAvailable = true
      })
    },
    mounted() {

    }
  }
</script>

<style scoped>
    .overtime {
        font-size: 1.5em;
        color: white;
        padding: 6em 0 8em 0;
    }
</style>
