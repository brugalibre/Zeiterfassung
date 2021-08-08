const setActualWorkingHoursApiUrl = "/api/v1/setactualworkinghours";
export default {
  name: 'setActualWorkingHours',
  data (){
    return {
      setActualWorkingPieChartData: {},
    }
  },
  methods: {
    fetchSetActualWorkingHoursDto: function(){
      fetch(setActualWorkingHoursApiUrl)
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
