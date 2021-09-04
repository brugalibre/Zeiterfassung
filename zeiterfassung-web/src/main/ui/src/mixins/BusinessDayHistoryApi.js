  const businessDayHistoryApiUrl = "/api/v1/businessday-history";
  export default {
  name: 'BusinessDayHistoryApi',
  data (){
    return {
      businessDayHistoryChartData: []
    }
  },
  methods: {
    fetchBusinessDayHistoryChartData : function(){
      fetch(businessDayHistoryApiUrl)
        .then(response => response.json())
        .then(data => {
          this.businessDayHistoryOverviewDto = data;
          this.businessDayHistoryDtos = this.businessDayHistoryOverviewDto.businessDayHistoryDtos;
          this.businessDayHistoryChartData = Array.from(this.businessDayHistoryDtos, businessDayHistoryDto => [businessDayHistoryDto.dateRepresentation, businessDayHistoryDto.bookedHours]);
      }).catch(error => console.error("Error occurred while fetching businessDay-history", error));
    },
  }
}
