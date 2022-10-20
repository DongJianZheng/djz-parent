<!--上传文件模板 -->
<template>
  <el-upload
    name="file"
    class="upload-demo"
    ref="upload"
    action="api/v1/attach/upload"
    :on-preview="handlePreview"
    :on-remove="handleRemove"
    :auto-upload="false"
    :disabled="true">
    <el-button size="small" icon="el-icon-upload2" @click="submitUpload">点击上传文件</el-button>
    <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
  </el-upload>


</template>
<script>
  export default {
    data() {
      return {
        fileList: []
      };
    },
    methods: {
      handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      handlePreview(file) {
        console.log(file);
      },
      submitUpload() {
        this.$refs.upload.submit();
      },
    }
  }
</script>
