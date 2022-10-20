<template>
  <div v-loading="loading">
    <div v-if="model==='table'">
      <div v-show="!tableList.length && !loading && canUpload">
        <div v-show="tplUrl && tplUrl.length" class="el-steps el-steps--simple">
          <div class="el-step is-simple" style="flex-basis: 100%; margin-right: 0px;">
            <div class="el-step__head is-finish">
              <div class="el-step__line" style="margin-right: 0px;">
                <i class="el-step__line-inner" style="transition-delay: 0ms; border-width: 0px; width: 100%;"></i>
              </div>
              <div class="el-step__icon is-icon"><i class="el-step__icon-inner"></i>
              </div>
            </div>
            <div class="el-step__main">
              <div class="el-step__title is-finish" style="color:#333333;">
                <div class="el-step__icon is-text" style="width: 24px;height: 24px;font-size: 16px;border-color:#3366ab;color:#FFFFFF;background-color: #3366ab;">
                  <div class="el-step__icon-inner">1</div>
                </div>
                下载数据填写模版
              </div>
              <div class="el-step__arrow"></div>
            </div>
          </div>
          <div class="el-step is-simple is-flex" style="flex-basis: 100%; max-width: 50%;">
            <div class="el-step__head is-finish">
              <div class="el-step__line">
                <i class="el-step__line-inner"></i>
              </div>
              <div class="el-step__icon is-icon">
                <i class="el-step__icon-inner"></i>
              </div>
            </div>
            <div class="el-step__main">
              <div class="el-step__title is-finish" style="color:#333333;">
                <div class="el-step__icon is-text" style="width: 24px;height: 24px;font-size: 16px;border-color:#3366ab;color:#FFFFFF;background-color: #3366ab;">
                  <div class="el-step__icon-inner">2</div>
                </div>
                上传填写好的数据文件
              </div>
              <div class="el-step__arrow"></div>
            </div>
          </div>
        </div>
      </div>
      <label :for="name" v-show="!tableList.length && !loading && canUpload">
        <div class="el-upload-dragger" style="margin: 0px auto;width:100%;"><i class="el-icon-upload"></i>
          <div class="el-upload__text" style="font-size: 16px;">
            <template v-if="$refs.upload && drop">
              将文件拖到此处，或点击
            </template>
            <em style="color: #3366ab;">浏览文件</em>
          </div>
        </div>
      </label>
      <div v-show="!tableList.length && !loading && showTips && tipsContent" style="font-size: 16px;padding-top:20px;">
        <slot name="tips">
          <span style="color: red;">*&nbsp;</span>
          <span style="color: rgb(118 120 124);">{{ tipsContent }}</span>
        </slot>
      </div>
      <!-- 附件展示 -->
      <el-form :model="appForm"  ref="appForm" label-width="0px" size="medium" inline :show-message="false">
        <div v-if="allowDownload && tableList.length" style="margin-bottom: 10px;">
          <el-button type="primary" :loading="downloadLoading" icon="el-icon-download" size="medium" @click="downloadFile()">批量下载</el-button>
        </div>
        <div  v-show="!tableList.length && !canUpload"  style="color: #757575;display: block;text-align: center;font-size: 16px;padding: 20px;">
          暂无附件信息
        </div>
        <el-table ref="table" v-show="tableList.length"
                  :data="tableList"
                  :max-height="500"
                  :border="showColumnExecutePhase"
                  :stripe="!showColumnExecutePhase"
                  @selection-change="rowSelection"
                  :span-method="mergeRowMethod"
                  style="width: 100%">
          <el-table-column
            v-if="showColumnCheckbox && allowDownload"
            type="selection"
            align="center"
            width="55">
          </el-table-column>
          <el-table-column
            v-if="showColumnExecutePhase"
            min-width="150"
            align="center"
            prop="executePhase"
            label="执行阶段"
          >
            <template slot-scope="scope">
              {{ getExecutePhaseTxt(scope.row.data.executePhase) }}
            </template>
          </el-table-column>
          <el-table-column
            v-if="showColumnFileType"
            min-width="150"
            align="center"
            prop="fileType"
            label="资料类别">
            <template slot-scope="scope">
              <el-form-item style="margin: auto;" v-if="!scope.row.success && !disableOption"
                            :required="!scope.row.success"
                            :prop="'files.'+scope.$index+'.data.fileType'"
              >
                <el-select :ref="scope.row.id" @change="fileTypeChange(scope.row)" v-model="scope.row.data.fileType" clearable :disabled="!scope.row.active && scope.row.success" class="tyleclass" style="width:100%" filterable placeholder="请选择">
                  <el-option
                    v-for="item in fileTypes"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <template v-else>
                {{ getFileTypeTxt(scope.row.data.fileType) }}
              </template>
            </template>
          </el-table-column>
          <el-table-column
            prop="name"
            min-width="200"
            label="资料名称">
            <template slot-scope="scope">
              <el-link style="font-size: 16px;" type="primary" :underline="false" v-if="allowDownload && scope.row.response && scope.row.response.data && scope.row.response.data.id" @click="downloadFile(scope.row)">{{ scope.row.name }}</el-link>
              <span v-else>{{ scope.row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            min-width="100"
            label="上传人">
            <template slot-scope="scope">
              {{ scope.row.response?(scope.row.response.data?scope.row.response.data.createRealName:''):'' }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            min-width="130"
            label="上传时间">
            <template slot-scope="scope">
              {{ scope.row.response?(scope.row.response.data?scope.row.response.data.createTime:''):'' }}
            </template>
          </el-table-column>
          <el-table-column
            v-if="showColumnStatus"
            align="center"
            min-width="80"
            label="上传状态">
            <template slot-scope="scope">
              <el-progress v-show="scope.row.active"  :text-inside="true" :stroke-width="14" :percentage="scope.row.progress" :status="processStatus(scope.row)"></el-progress>
              <span v-show="!scope.row.active && scope.row.error && !scope.row.success" style="color: red;">{{ getErrMsg(scope.row.error,scope.row) }}</span>
              <i class="el-icon-check el-button--success" v-show="!scope.row.active && scope.row.success"></i>
            </template>
          </el-table-column>

          <el-table-column
            v-if="canDel || canUpload || canPreview || allowDownload"
            label="操作"
            align="center"
          >
            <template slot-scope="scope">
              <el-tooltip class="item" content="重新选择文件" placement="top" v-show=" canUpload && rowSelectShow">
                <el-link style="margin: auto 5px;font-size: 18px;" :underline="false" icon="el-icon-folder-opened" @click="reSelectFile(scope.row)"></el-link>
              </el-tooltip>
              <el-tooltip class="item"  content="重新上传" placement="top" v-show="scope.row.error && scope.row.error !== 'compressing' && scope.row.error !== 'image parsing' && $refs.upload.features.html5" >
                <el-link style="margin: auto 5px;font-size: 18px;" :underline="false" icon="el-icon-refresh" @click="submitFile(scope.row)"></el-link>
              </el-tooltip>
              <el-tooltip class="item"  content="删除文件" placement="top"  v-show="!scope.row.active && (!scope.row.success || canDel)">
                <el-link style="margin: auto 5px;font-size: 18px;" :underline="false" icon="el-icon-delete" @click="remove(scope.row)"></el-link>
              </el-tooltip>
              <el-tooltip class="item"  content="预览" placement="top"   v-show="allowPreview && scope.row.success && scope.row.response && scope.row.response.data && scope.row.response.data.id">
                <el-link style="margin: auto 5px;font-size: 18px;" :underline="false" icon="el-icon-view" @click="preview(scope.row)"></el-link>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
        <input type="file" v-show="false" @change="selectedFile" :accept="accept" ref="singleInput"/>
      </el-form>

      <div style="margin-top: 30px;text-align: center;" v-show="!loading && canUpload">
        <!-- 模版下载 -->
        <el-button :loading="tplLoading" size="medium" v-if="tplUrl && !(tplUrl instanceof Array)" icon="el-icon-download" @click="downloadTpl({ url: tplUrl, single: true})" style="margin-right: 10px;">模版下载</el-button>
        <el-dropdown size="medium" v-if="tplUrl && tplUrl instanceof Array" split-button trigger="hover" type="default" style="margin-right: 10px;">
          上传数据模版下载
          <el-dropdown-menu  slot="dropdown">
            <el-dropdown-item  :key="i" @click.native="downloadTpl(tp)" v-for="(tp,i) in tplUrl">{{ tp.name }}</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

        <!-- 浏览文件 -->
        <label :for="name" v-if="canUpload" type="button" class="el-button el-button--default el-button--medium" size="medium" style="margin-right: 10px;">
          <i class="el-icon-folder-opened"></i>
          浏览文件
        </label>
        <el-button v-permission="['file:upload']" v-if="canUpload && (!$refs.upload || !$refs.upload.active)" size="medium"  :disabled="!hasUnloadFile" type="primary" icon="el-icon-upload2" @click="submitFile(null)">开始上传</el-button>
        <el-button v-permission="['file:upload']" v-if="canUpload && $refs.upload && $refs.upload.active" size="medium"  type="primary" icon="el-icon-video-pause" @click.prevent="$refs.upload.active = false" >停止上传</el-button>
      </div>
    </div>
    <input type="file" v-show="false" :multiple="multiple" @change="browFileResult" :accept="accept" ref="browFileRef"/>
    <div v-if="model==='tab'" style="display: inline-block;">
      <el-form :model="appForm"  ref="appForm" label-width="0px" size="medium" inline :show-message="false">
        <slot :files="tableList" :canDel="checkDel" :remove="remove">
          <el-tag
                  v-show="tableList.length"
                  :style="{ margin: '2px 10px 2px 10px',fontSize: '16px',cursor: canPreview?'pointer':''}"
                  :key="tag.id"
                  v-for="tag in tableList"
                  :closable="checkDel(tag)"
                  :disable-transitions="false"
                  @close="remove(tag)"
                  @click="preview(tag)">
            {{ tag.name }}
            <span v-show="tag.active">
                  {{ tag.progress }}%
              </span>
            <i class="el-icon-warning-outline el-button--danger" v-show="!tag.active && tag.error && !tag.success"></i>
            <i class="el-icon-check el-button--success" v-show="!tag.active && tag.success"></i>
          </el-tag>
        </slot>
        <label :for="name" v-if="canUpload">
          <slot name="browFile">
            <el-link type="primary" icon="el-icon-upload" style="font-size: 16px;" :underline="false">浏览文件</el-link>
          </slot>
        </label>
      </el-form>
    </div>
    <!-- 上传组件 -->
    <file-upload
      v-show="false"
      ref="upload"
      :name="name"
      :input-id="name"
      v-model="appForm.files"
      :add-index="true"
      :accept="accept"
      :extensions="extensions"
      :multiple="multiple"
      :size="size"
      :thread="thread"
      :maximum="maximum"
      :timeout="timeout"
      :drop="drop"
      :post-action="impUrl"
      :headers="headers"
      :data="data"
      @input-filter="inputFilter"
      @input-file="inputFile"
    >
    </file-upload>
  </div>
</template>

<script>
import { getToken } from '@/utils/auth'
import FileUpload from 'vue-upload-component'
import dictUtils from 'ecip-web/utils/dictUtils'
import { findAttachByBizIed } from './AttachApi'
import AttachProps from './AttachProps'
import request from 'ecip-web/utils/request'

export default {
  name: 'AttachList',
  mixins: [AttachProps],
  components: { FileUpload },
  data() {
    return {
      appForm: {
        files: [],  // 已上传的文件
        uploadIds: [],  // 已上传的文件ID
        attachs: [] // 服务端加载的文件
      },
      replaceFile: null,
      deleteIds: [], // 删除的附件ID集合
      headers: { 'token': getToken() },
      loading: false,
      selectRows: [],
      downloadLoading: false,
      // singleMaximum: 0,
      needTips: false,
      tplLoading: false,
      notifyPromise: Promise.resolve()
    }
  },
  computed: {
    tableList() {
      if (this.dataFilter) {
        return this.dataFilter(this.appForm.files, this.trackParam)
      }
      return this.appForm.files
    },
    isChange() {
      return this.bizId + '_' + this.reqSeq + '_' + this.bizIds
    },
    hasUnloadFile() {
      if (this.appForm.files.length) {
        for (const i in this.appForm.files) {
          const f = this.appForm.files[i]
          if (!f.success) {
            return true
          }
        }
      }
      return false
    },
    fileTypes() {
      let arr = []
      const dics = this.$dictUtils.getDictList('SP_FILE_TYPE')
      if (this.bizType && dics) {
        arr = dics.filter(x => x.value.substring(0, 2) == this.bizType)
        if (this.isFilterOption && this.typeMaximum && this.typeMaximum.length) {
          const fileTypes = this.typeMaximum.map(x => x.fileType + '')
          arr = arr.filter(x => fileTypes.indexOf(x.value) > -1)
        }
      }
      return arr
    },
    allowDownload() {
      return this.canDownload && this.$store.getters.roles.indexOf('file:download') > -1
    },
    allowPreview() {
      return this.canPreview && this.$store.getters.roles.indexOf('file:download') > -1
    }
  },
  async mounted() {
    if (this.mountedQuery) {
      await this.load()
    }
  },
  watch: {
    isChange(val) {
      this.$nextTick(async() => {
        if (this.bizId || this.bizIds) {
          await this.load()
        } else {
          this.init()
          // 执行一次回调
          this.$emit('complete', this.getResult(this.getFiles(), true), this.getFiles())
        }
      })
    }
  },
  methods: {
    init: function() {
      this.deleteIds = []
      this.appForm.files = []
      this.appForm.attachs = []
      this.appForm.uploadIds = []
      this.replaceFile = null
      this.$emit('update:deleteIds', this.deleteIds)
      this.updateFiles()
    },
    updateFiles: function() {
      if (this.dataFilter) {
        this.$emit('update:files', this.dataFilter(this.appForm.files, this.trackParam))
      } else {
        this.$emit('update:files', this.appForm.files)
      }
    },
    getFiles: function() {
      if (this.dataFilter) {
        return this.dataFilter(this.appForm.files, this.trackParam)
      } else {
        return this.appForm.files
      }
    },
    load: async function() {
      if (this.bizId || (this.bizIds && this.bizIds.length)) {
        this.needTips = false
        this.loading = true
        let result = { data: [] }
        // 获取附件信息
        if (!this.queryUrl && this.$store.getters.roles.indexOf('file:list') === -1) {
          // 没有数据权限
          this.$notify.warning('没有查询附件权限，无法查到附件信息')
        } else {
          result = await this.getAttachs()
        }
        if (result.data) {
          this.init()
          this.appForm.attachs = result.data
          // 追加已上传的附件
          for (const f of this.appForm.attachs) {
            if (this.appForm.uploadIds.indexOf(f.id) === -1) {
              this.appForm.files.push({
                active: false,
                success: true,
                isSys: true,
                name: f.fileName,
                size: f.fileSize,
                data: {
                  projectStage: f.projectStage + '',
                  fileType: f.fileType + '',
                  bizType: f.bizType + '',
                  remark: f.remark,
                  subFileType: f.subFileType,
                  executePhase: f.executePhase,
                  dataType: f.dataType
                },
                response: {
                  data: f
                }
              })
            }
          }
           this.updateFiles()
          // 执行一次回调
          this.$emit('complete', this.getResult(this.getFiles(), true), this.getFiles())
          this.loading = false
          return
        }
      }
      this.init()
    },
    async downloadTpl({ name, url, single }) { // 下载模板
      if (single) {
        try {
          this.tplLoading = true
          await this.download({ url: url, params: { name: name, projectId: this.projectId }})
        } finally {
          this.tplLoading = false
        }
      } else {
        this.download({ url: url, params: { name: name, projectId: this.projectId }})
      }
    },
    async downloadFile(file) { // 下载
      let ids = []
      if (file) {
        ids.push(file.response.data.id)
      } else {
        ids = this.selectRows.filter(x => x.response.data && x.response.data.id).map(x => x.response.data.id)
      }
      if (!ids.length) {
        this.$notify.warning('请选择要下载的文件')
        return
      }
      try {
        this.downloadLoading = true
        await this.download({ url: 'api/v1/attach/download', params: { ids: ids.join(',') }})
      } finally {
        this.downloadLoading = false
      }
    },
    selectedFile: function() {
      try {
        this.$refs.upload.addInputFile(this.$refs.singleInput)
      } finally {
        this.$refs.singleInput.value = null
      }
    },
    browFileResult: function() {
      try {
        this.$refs.upload.addInputFile(this.$refs.browFileRef)
      } finally {
        this.$refs.browFileRef.value = null
      }
    },
    reSelectFile: function(file) {
      this.replaceFile = file
      this.$refs.singleInput.click()
    },
    remove: function(file) {
      // 移除附件
      const res = file.response.data
      if (file.isSys) {
        this.appForm.files.forEach(({ response }, idx) => {
          if (response.data && response.data.id === res.id) {
            this.appForm.files.splice(idx, 1)
            return
          }
        })
      } else {
        this.$refs.upload.remove(file)
      }
      if (res) {
        this.deleteIds.push(res)
        this.$emit('update:deleteIds', this.deleteIds)
      }
       this.updateFiles()
      this.$emit('complete', this.getResult(this.getFiles(), true), this.getFiles(), null, 'remove')
    },
    preview: function(row) {
      if (this.allowPreview && row.success && row.response && row.response.data && row.response.data.id) {
        window.open(`#/preview?attachId=${row.response.data.id}`)
      }
    },
    checkDel: function(file) {
      if (!file) return false
      return !file.active && (!file.success || this.canDel)
    },
    clear: function() {
      this.appForm.files = []
    },
    getResult: function(files, isLastReturn, errMsg) {
      // 所有文件上传完毕
      const addAttachIds = []
      const hasAttachIds = []
      let delAttachIds = []
      for (const f of files) {
        if (!f.success) {
          continue
        }
        if (!f.response.data) continue
        let at = f.response.data.id
        if (!this.isReturnId) {
          at = f.response.data
        }
        if (f.isSys) {
          hasAttachIds.push(at)
        } else {
          addAttachIds.push(at)
        }
      }
      if (this.isReturnId) {
        this.deleteIds.forEach(x => {
          delAttachIds.push(x.id)
        })
      } else {
        delAttachIds = [...this.deleteIds]
      }
      const returnObj = {
        addAttachIds: addAttachIds, // 新上传的
        hasAttachIds: isLastReturn ? hasAttachIds : null, // 已上传的
        delAttachIds: isLastReturn ? delAttachIds : null, // 需要删除的
        errorMsgList: []
      }
      if (this.trackParam) {
        returnObj['trackParam'] = this.trackParam
      }
      if (errMsg && errMsg.length) {
        returnObj['errorMsgList'] = errMsg
      }
      return returnObj
    },
    uploadFile: async function(file) {
      if (file) {
        this.$refs.upload.update(file, { active: true, error: '', progress: '0.00' })
      } else {
        // 自动重试上传失败的
        for (const f of this.appForm.files) {
          if (f.error) {
            this.$refs.upload.update(f, { error: '', progress: '0.00' })
          }
        }
        if (!this.appForm.files || !this.appForm.files.length) {
          if (this.required) {
            this.$notify.warning('请选择要上传的文件')
          } else {
            this.$emit('complete', this.getResult(this.getFiles(), true), this.getFiles())
          }
          return
        }
        let hasFielUpload = false
        for (const f of this.appForm.files) {
          if (!f.success) {
            hasFielUpload = true
            break
          }
        }
        if (!hasFielUpload && this.model === 'tab') {
          this.$emit('complete', this.getResult(this.getFiles(), true), this.getFiles())
        }

        this.$refs.upload.active = true
      }
    },
    submitFile: function(file) {
      this.$refs.appForm.validate((valid) => {
        if (valid) {
          // 检验文件类型数量
          if (this.typeMaximum && this.typeMaximum.length) {
            const types = this.typeMaximum.filter(x => x.required)
            const losFile = []
            types.forEach(type => {
              const hasFile = this.appForm.files.filter(x => this.isSamTypeFile(x, type)).length
              if (!hasFile) {
                losFile.push(this.getFileTypeTxt(type.fileType))
              }
            })
            if (losFile.length) {
              this.$notify.warning(`至少要上传资料类别为[${losFile.join(',')}]的文件`)
              return
            }
          }
          this.uploadFile(file)
        } else {
          this.$notify.warning('请选择文件的资料类别')
          return
        }
      })
    },
    processStatus: function(file) {
      if (file.error) return 'exception'
      if (file.success) return 'success'
      return 'success'
    },
    // 转化提示内容
    getErrMsg: function(error) {
      if (error === 'size') return `超出文件大小[${this.size/1024/1024}M]`
      if (error === 'extension') return `只支持后缀文件[${this.extensions}]`
      if (error === 'network') return '网络错误'
      if (error === 'abort') return '上传错误'
      if (error === 'server') return '上传错误'
      return error
    },
    setFileAttr: function(file) {
      // 设置上传的表单ID
      file.data.inputId = this.name
      // 设置项目ID
      file.data.projectId = this.projectId
      // 设置关联的bizId,
      if (!this.bizIdOnlyQuery) {
        file.data.bizId = this.bizId
      }
      // 设置业务分类
      file.data.bizType = this.bizType
      // 设置附件备注
      file.data.remark = this.remark
      // 设置数据类型
      file.data.dataType = this.dataType
      // 设置附件子类别
      file.data.subFileType = this.subFileType
      // 设置执行阶段
      file.data.executePhase = this.executePhase
      // 设置执行阶段
      file.data.projectStage = this.projectStage
      // 设置文件类别
      if (this.fileType) {
        file.data.fileType = this.fileType
      }
      // 设置bizType
      if (!file.data.fileType && this.bizType) {
        this.fileTypes.forEach(x => {
          if (file.name.indexOf(x.extra) > -1) {
            file.data.fileType = x.value
            return
          }
        })
        if (!file.data.fileType && this.fileTypes.length > 0) {
          file.data.fileType = this.fileTypes[this.fileTypes.length - 1].value
        }
      }
      // 自动设置关联的 子文件类别
      this.setFileProp(file)
    },
    setFileProp(file) {
      // 自动设置关联的 子文件类别
      if (file.data.fileType && this.typeMaximum && this.typeMaximum.length) {
        const types = this.typeMaximum.filter(x => x.fileType == file.data.fileType)
        if (types.length) {
          const type = types[0]
          if (!file.data.subFileType) {
            file.data.subFileType = type.subFileType || this.subFileType
          }
          if (!file.data.remark) {
            file.data.remark = type.remark || this.remark
          }
          if (!file.data.executePhase) {
            file.data.executePhase = type.executePhase || this.executePhase
          }
        }
      }
    },
    inputFilter: function(newFile, oldFile, prevent) {
      // 添加文件
      if (newFile && !oldFile) {
        if (this.maximum && this.appForm.files.length) {
          if (this.appForm.files.length >= this.maximum) {
            this.$notify.warning(`系统只允许上传${this.maximum}个文件`)
            return prevent()
          }
        }

        // 逐个替换文件
        if (this.replaceFile) {
          this.remove(this.replaceFile)
          newFile.data.fileType = null
          if (this.replaceFile.data.fileType) {
            newFile.data.fileType = this.replaceFile.data.fileType + ''
          }
          this.replaceFile = null
        }
        // 设置资料类型
        this.setFileAttr(newFile)

        // 检查多类型文件上传的数量
       /* if (this.singleMaximum) {
          const samlen = this.appForm.files.filter(x => this.isSamTypeFile(x)).length
          if (samlen >= this.singleMaximum) {
            this.$notify.warning(`系统只允许上传${this.singleMaximum}个文件`)
            return prevent()
          }
        }*/

        // 附件类型文件限制
        const fileKey = newFile.data.fileType + '_' + newFile.data.subFileType + '_' + newFile.data.remark
        // 检查多文件类型
        if (this.typeMaximum && this.typeMaximum.length) {
          let hasLimit = false
          this.typeMaximum.forEach(x => {
            if (!x.key) {
              x.key = x.fileType + '_' + x.subFileType + '_' + x.remark
            }
            if (x.key === fileKey) {
              const samlen = this.appForm.files.filter(f => this.isSamTypeFile(f, x)).length
              if (x.maximum && samlen >= x.maximum) {
                hasLimit = x
                return
              }
            }
          })
          if (hasLimit) {
            this.$notify.warning(`系统只允许上传${hasLimit.maximum}个[${this.getFileTypeTxt(hasLimit.fileType)}]文件`)
            return prevent()
          }
        }

        this.needTips = true
        // 创建 `blob` 字段 用于缩略图预览
        newFile.blob = ''
        let URL = window.URL || window.webkitURL
        if (URL && URL.createObjectURL) {
          newFile.blob = URL.createObjectURL(newFile.file)
        }
        // 回写文件
        this.$nextTick(() => {
           this.updateFiles()
        })
      }
    },
    // 文件上传完毕
    inputFile: function(newFile, oldFile) {
      if (newFile && oldFile) {
        // 上传错误
          if (newFile.error !== oldFile.error) {
            const errres = newFile.response
            if (errres.data && errres.data.length) {
              const msglist = []
              for (const err of errres.data) {
                msglist.push(err.colMsg)
              }
              this.$refs.upload.update(newFile, { error: msglist.join(',') })
            }
          }

        // 开始上传
        if (newFile.active !== oldFile.active) {
          // 限定最大字节
          if (newFile.size > this.size) {
            newFile = this.$refs.upload.update(newFile, { error: 'size' })
          }
        }

        // 更新进度
        if (newFile.progress !== oldFile.progress) {
          this.$nextTick(() => {
             this.updateFiles()
          })
        }

        // 每个文件上传成功
        if (newFile.success && !oldFile.success) {
          if (newFile.response.data) {
            // 每次上传成功后返回，直接返回
            if (newFile.response.data.id) {
              this.appForm.uploadIds.push(newFile.response.data.id)
            }
            if (this.singleReturn) {
              this.updateFiles()
              this.$emit('complete', this.getResult([newFile], false), [newFile], true)
            }
          }
        }
        // 所有文件上传完成
        if (this.$refs.upload.uploaded) {
          this.updateFiles()
          this.$nextTick(() => {
            let hasError = false
            let hasSuccess = false
            const msg = []
            this.appForm.files.forEach(x => {
              if (x.error) {
                hasError = true
                if (!newFile.active && oldFile.active) {
                  if (x.response.code === 501) {
                    msg.push(`上传错误【${x.name}】：${x.response.message}`)
                  } else {
                    msg.push(`上传错误【${x.name}】：${this.getErrMsg(x.error)}`)
                  }
                }
                return
              }
              if (x.success) {
                hasSuccess = true
              }
            })
            // 错误提醒
            if (hasError) {
              if (msg.length) {
                msg.forEach(m => {
                  this.notifyPromise = this.notifyPromise.then(() => {
                    this.$notify.error(m)
                  })
                })
              }
              // 全部上传错误，无需回调
              if (!hasSuccess) {
                return
              }
            }
            const result = this.getResult(this.getFiles(), true, msg)
            if (!hasError && this.needTips) {
              this.needTips = false
              this.$notify.success('文件上传完成')
            }
            this.$emit('complete', result, this.appForm.files)
          })
        }
      }
    },
    // 获取附件信息
    getAttachs: function() {
      const param = {}
      Object.assign(param, this.queryParam)
      param['bizId'] = this.bizId
      if (this.bizIds && this.bizIds.length) {
        param['bizIds'] = this.bizIds
      }
      return findAttachByBizIed(param, this.queryUrl)
    },
    getFileTypeTxt: function(type) {
      // console.log(type)
      if (!type) return '未分类'
      return dictUtils.getDictLabel('SP_FILE_TYPE', type, '')
    },
    getExecutePhaseTxt: function(cellValue) {
      if (!cellValue) return ''
      return dictUtils.getDictLabel('executePhase', cellValue, '')
    },
    rowSelection: function(rows) {
      this.selectRows = rows
    },
    browFile: function(param) {
      this.bizType = param.bizType || this.bizType
      this.projectStage = param.projectStage || this.projectStage
      this.fileType = param.fileType || this.fileType
      this.subFileType = param.subFileType || this.subFileType
      this.remark = param.remark || this.remark
      this.executePhase = param.executePhase || this.executePhase
      this.$refs.browFileRef.click()
    },
    isSamTypeFile: function(file, fileParam) {
      if (!fileParam) {
        if (this.bizType && this.bizType != file.data.bizType) return false
        if (this.projectStage && this.projectStage != file.data.projectStage) return false
        if (this.fileType && this.fileType != file.data.fileType) return false
        if (this.subFileType && this.subFileType != file.data.subFileType) return false
        if (this.executePhase && this.executePhase != file.data.executePhase) return false
        if (this.remark && this.remark != file.data.remark) return false
        return true
      } else {
        if (fileParam.bizType && fileParam.bizType != file.data.bizType) return false
        if (fileParam.projectStage && fileParam.projectStage != file.data.projectStage) return false
        if (fileParam.fileType && fileParam.fileType != file.data.fileType) return false
        if (fileParam.subFileType && fileParam.subFileType != file.data.subFileType) return false
        if (fileParam.executePhase && fileParam.executePhase != file.data.executePhase) return false
        if (fileParam.remark && fileParam.remark != file.data.remark) return false
        return true
      }
    },
    fileTypeChange(row) {
      if (this.typeMaximum && this.typeMaximum.length) {
        const preVal = this.$refs[row.id].value
        const fileType = row.data.fileType
        const fsize = this.appForm.files.filter(x => x.data.fileType == fileType).length
        const tp = this.typeMaximum.filter(x => x.fileType == fileType)
        if (tp.length && tp[0].maximum && fsize > tp[0].maximum) {
          row.data.fileType = preVal
          this.$notify.warning(`系统只允许上传${tp[0].maximum}个[${tp[0].label}]文件，无法更改资料类别`)
        }
        this.setFileProp(row)
      }
    },
    download: function(option) {
      return new Promise((resolve, reject) => {
        const options = Object.assign({ method: 'get', responseType: 'blob' }, option)
        request(options).then(response => {
          if (!response) {
            reject('下载失败')
            return
          }
          let filename = response.headers['content-disposition']
          if (!filename) {
            const fileReader = new FileReader()
            fileReader.readAsText(response.data, 'utf-8')
            fileReader.onloadend = () => {
              const jsonData = JSON.parse(fileReader.result)
              this.$notify.error(jsonData.message || '下载失败')
            }
            reject('下载失败')
            return
          }
          if (filename.indexOf('filename=') > -1) {
            filename = filename.split('filename=')[1]
          }
          if (filename.indexOf('%') > -1) {
            filename = decodeURI(filename)
          } else {
            try {
              filename = decodeURIComponent(escape(filename))
            } catch (error) {
              filename = decodeURI(filename)
            }
          }
          if (window.navigator && window.navigator.msSaveOrOpenBlob) { // IE
            window.navigator.msSaveOrOpenBlob(new Blob([response.data]), filename)
          } else {
            const objectUrl = (window.URL || window.webkitURL).createObjectURL(new Blob([response.data]))
            const downFile = document.createElement('a')
            downFile.style.display = 'none'
            downFile.href = objectUrl
            downFile.download = filename // 下载后文件名
            document.body.appendChild(downFile)
            downFile.click()
            document.body.removeChild(downFile) // 下载完成移除元素
            // window.location.href = objectUrl
            window.URL.revokeObjectURL(objectUrl) // 只要映射存在，Blob就不能进行垃圾回收，因此一旦不再需要引用，就必须小心撤销URL，释放掉blob对象。
          }
          resolve('下载成功')
          // eslint-disable-next-line handle-callback-err
        }).catch((error) => {
          // console.log(error)
          this.$notify.error('下载失败')
          reject('下载失败')
        })
      })
    },
    mergeRowMethod({ row, column, rowIndex, columnIndex }) {
      const fields = ['executePhase']
      if (this.showColumnExecutePhase) {
        fields.push('fileType')
      }
      if (column.property && fields.includes(column.property)) {
        const cellValue = row.response.data[column.property]
        const prevRow = this.tableList[rowIndex - 1]
        let nextRow = this.tableList[rowIndex + 1]
        if (prevRow && prevRow.response.data[column.property] === cellValue && prevRow.response.data['executePhase'] === row.response.data['executePhase']) {
          return {rowspan: 0, colspan: 0}
        } else {
          let countRowspan = 1
          while (nextRow && nextRow.response.data[column.property] === cellValue && nextRow.response.data['executePhase'] === row.response.data['executePhase']) {
            nextRow = this.tableList[++countRowspan + rowIndex]
          }
          if (countRowspan > 1) {
            return { rowspan: countRowspan, colspan: 1 }
          }
        }
      }
    }
  }
}
</script>
<style src="../../../projectManage/common/grid.css" scoped />
<style scoped>
.el-form >>> .el-input{
  min-width: auto;
  width: 100%;
}
.el-form >>> .el-form-item__label, .el-form >>> .el-input--medium{
  font-size: 16px;
}
.el-form >>> .el-tag {
  white-space: normal;
  line-height: normal;
  padding: 5px 10px;
  height: auto;
}
</style>
