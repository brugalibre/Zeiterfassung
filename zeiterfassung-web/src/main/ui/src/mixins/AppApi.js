  export default {
  name: 'AppApi',
  methods: {
    requestUiRefresh: function(){
      this.$emit('refreshUi');
    },
    refreshUiWithHistory: function(){
      this.$emit('refreshUiWithHistory');
    },
  }
}
