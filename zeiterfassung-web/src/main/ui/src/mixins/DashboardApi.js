const dashboardApiUrl = "/api/v1/dashboard";
export default {
  name: 'DashboardApi',
  data() {
    return {
      applicationTitle: ''
    }
  },
  methods: {
    fetchApplicationTitle: function () {
      fetch(dashboardApiUrl)
        .then(response => response.json())
        .then(data => {
          this.applicationTitle = data.dashboardTitle;
        });
    },
  }
}
