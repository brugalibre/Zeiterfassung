  export default {
  name: 'AppApi',
  methods: {
    requestUiRefresh: function(){
      this.$emit('refreshUi', false);
    },
  }
}
