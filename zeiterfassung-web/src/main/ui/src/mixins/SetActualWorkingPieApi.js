const businessDayConfigApiUrl = "/api/v1/businessdayconfig";
export default {
  name: 'setActualWorkingPieApi',
  data (){
    return {
      setActualWorkingPieChartData: {},
    }
  },
  methods: {
    fetchSetActualWorkingPieChartData: function(){
      fetch(businessDayConfigApiUrl)
        .then(response => response.json())
        .then(data => {
          this.setActualWorkingHoursDto = data;
          this.setActualWorkingPieChartData =
            [['Rest Stunden', this.setActualWorkingHoursDto.hoursLeft],
          ['Bereits geleistete Stunden', this.setActualWorkingHoursDto.actualHours]];
      });
    },
  }
}
