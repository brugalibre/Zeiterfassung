const businessDayConfigApiUrl = "/api/v1/businessdayconfig";
export default {
  name: 'setActualWorkingPieApi',
  data() {
    return {
      setActualWorkingPieChartData: {},
      setHours: 0,
    }
  },
  methods: {
    fetchSetActualWorkingPieChartData: function () {
      fetch(businessDayConfigApiUrl)
        .then(response => response.json())
        .then(data => {
          this.setActualWorkingHoursDto = data;
          this.setActualWorkingPieChartData =
            [['Bereits geleistete Stunden (' + this.setActualWorkingHoursDto.actualHours + ')', this.setActualWorkingHoursDto.actualHours],
              ['Rest Stunden (' + this.setActualWorkingHoursDto.hoursLeft + ')', this.setActualWorkingHoursDto.hoursLeft]];
          this.setHours = this.setActualWorkingHoursDto.setHours;
        });
    },
  }
}
