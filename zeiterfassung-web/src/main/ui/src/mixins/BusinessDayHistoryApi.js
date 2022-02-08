const businessDayHistoryApiUrl = "/api/v1/businessday-history";
export default {
  name: 'BusinessDayHistoryApi',
  methods: {
    fetchBusinessDayHistoryChartData: function () {
      fetch(businessDayHistoryApiUrl)
        .then(response => response.json())
        .then(data => this.$store.dispatch('setBusinessDayHistoryOverviewDto', data))
        .catch(error => console.error("Error occurred while fetching businessDay-history", error));
    },
  }
}
