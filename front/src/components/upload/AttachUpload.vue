<template>
  <div>
    <slot :open="open" :loading="tplLoading" :download="download" :result="result" :files="files">
      <el-link type="primary"  :underline="false" @click="visible=true">{{ buttonText }}</el-link>
    </slot>
    <el-dialog ref="dialog"
      :title="`${title}`"
      :visible="visible"
      :width="width"
      @opened="dialogOpened"
      @close="close"
    >
      <attach-list ref="attach"
                   :tpl-url="tplUrl"
                   :imp-url="impUrl"
                   :can-del="canDel"
                   :can-upload="canUpload"
                   :can-download="canDownload"
                   :show-column-status="showColumnStatus"
                   :show-column-file-type="showColumnFileType"
                   :show-column-checkbox="showColumnCheckbox"
                   :disable-option="disableOption"
                   :single-return="singleReturn"
                   :row-select-show="rowSelectShow"
                   :is-return-id="isReturnId"
                   :project-id="projectId"
                   :biz-id="bizId"
                   :biz-type="bizType"
                   :biz-id-only-query="bizIdOnlyQuery"
                   :file-type="fileType"
                   :sub-file-type="subFileType"
                   :remark="remark"
                   :execute-phase="executePhase"
                   :req-seq="reqSeq"
                   :mounted-query="mountedQuery"
                   :track-param="trackParam"
                   :query-param="queryParam"
                   :model="model"
                   :name="name"
                   :input-id="name"
                   :accept="accept"
                   :extensions="extensions"
                   :multiple="multiple"
                   :thread="thread"
                   :maximum="maximum"
                   :timeout="timeout"
                   :drop="drop"
                   :post-action="impUrl"
                   :data="data"
                   :files.sync="files"
                   :delete-ids.sync="deleteIds"
                   :typeMaximum="typeMaximum"
                   :is-filter-option="isFilterOption"
                   :show-tips="showTips"
                   :tips-content="tipsContent"
                   :data-filter="dataFilter"
                   :data-type="dataType"
                   :project-stage="projectStage"
                   @complete="onComplete"/>
    </el-dialog>
  </div>
</template>

<script>
import AttachList from './AttachList'
import AttachProps from './AttachProps'

export default {
  name: 'AttachView',
  mixins: [AttachProps],
  // extend: AttachList,
  props: {
    title: { //  ???????????????
      type: String,
      default: '????????????',
      required: false
    },
    width: {
      type: String,
      default: '60%',
      required: false
    },
    openClear: { //  ??????dialog ????????????????????????????????????
      type: Boolean,
      default: false,
      required: false
    },
    visible: { //  dialog????????????
      type: Boolean,
      default: false,
      required: false
    },
    autoClose: { //  ??????????????????????????????
      type: Boolean,
      default: true,
      required: false
    },
    buttonText: { //  ????????????
      type: String,
      default: '????????????',
      required: false
    }
  },
  components: { AttachList },
  data() {
    return {
      files: [],
      deleteIds: [], // ???????????????ID??????
      result: null,
      isReturn: false, // ??????????????????dialog????????????????????????
      isLoad: false,
      canClose: true,
      tplLoading: false,
      isOpened: false
    }
  },
  watch: {
    isChange(val) {

    }
  },
  async mounted() {
    this.$refs.dialog.rendered = true
    // this.onComplete(this.getResult(this.appForm.files, true), this.appForm.files)
  },
  methods: {
    open() {
      this.visible = true
    },
    async download() {
      try {
        this.tplLoading = true
        await this.$refs.attach.downloadTpl({ url: this.tplUrl, single: true })
      } finally {
        this.tplLoading = false
      }
    },
    async dialogOpened() {
      if (this.openClear) {
        this.$refs.attach.clear()
      }
      this.canClose = true
      if (!this.mountedQuery && !this.isLoad) {
        this.isLoad = true
        this.canClose = false
        await this.$refs.attach.load()
        if (!this.files.length) {
          this.canClose = true
        }
        this.isOpened = true
      }
    },
    close() {
      this.visible = false
      this.$emit('update:visible', this.visible)
      this.$emit('close')
    },
    onComplete(result, files, isSingle, from) {
      // ???????????????????????????
      if (isSingle) {
        this.$emit('onSuccess', result, files)
        return
      }
      this.result = result
      this.files = files || []
      // ????????????????????????
      let hasError = false
      for (const f of this.files) {
        if (!f.success) {
          hasError = true
          break
        }
      }

      if (!hasError) {
        if (!from && this.autoClose && this.files.length && this.canClose) {
          this.visible = false
        }
      }
      if (this.isOpened) {
        this.canClose = true
      }
      this.$emit('onSuccess', this.result, this.files)
    }
  }
}
</script>
