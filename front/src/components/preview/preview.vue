<template>
  <div style="height: calc(100%);">
    <iframe  id="previewIfram" :src="previewUrl" width="100%" frameborder="0" style="height: calc(100%);" />
    <div v-show="loadingShow" style="width:300px;height:100px;margin: auto; top: 0; left: 0; bottom: 0; right: 0;text-align: center;position: absolute;line-height: 100px">
      <img :src="loadingUrl" style="vertical-align: middle; height: 50px;color: black!important;"><span style="margin-left: 10px;font-size: 16px;color: black!important;">{{ loadingContent }}</span>
    </div>
  </div>
</template>

<script>
  import { checkPreview } from './previewApi'
  export default {
    name: 'preview',
    components: {},
    data () {
      return {
        previewUrl: '',
        loadingUrl: location.href.split('#')[0] + '/static/preview/pptx/img/loader_indicator_lite.gif',
        loadingShow: false,
        loadingContent: '加载中...'
      }
    },
    watch: {
      $route: {
        handler: async function (route) {
          const res = await checkPreview({ attachId: route.query.attachId })

          if (res.data === 0) {
            this.loadingContent = '首次转换中，请稍后...'
            this.loadingShow = true
          } else if (res.data === 1) {
            this.loadingContent = '加载中...'
            this.loadingShow = true
          }

          this.previewUrl = location.href.split('#')[0] + `api/v1/preview/onlinePreview?attachId=${route.query.attachId}`
          this.$nextTick(function () {
            var iframe = document.getElementById('previewIfram')
            const that = this
            if (iframe.attachEvent) {
              iframe.attachEvent('onload', function () {
                that.loadingShow = false
              })
            } else {
              iframe.onload = function () {
                that.loadingShow = false
              }
            }
          })
        },
        immediate: true
      }
    },
    mounted () {
    },
    created () {

    },
    methods: {
    }
  }
</script>
