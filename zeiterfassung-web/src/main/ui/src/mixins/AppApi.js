const timerecorderApiUrl = "/api/v1/timerecorder";

export default {
  name: 'AppApi',
  data() {
    return {
      applicationTitle: ''
    }
  },
  methods: {
    requestUiRefresh: function () {
      this.$emit('refreshUi');
    },
    refreshUiWithHistory: function () {
      this.$emit('refreshUiWithHistory');
    },
    fetchApplicationTitle: function () {
      fetch(timerecorderApiUrl + '/getApplicationTitle')
        .then(response => response.json())
        .then(data => {
          console.log('application title: ' + data.applicationTitle);
          this.applicationTitle = data.applicationTitle;
        });
    }
  }
}
