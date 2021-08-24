export default {
  name: 'date-calculations',
  methods: {
    getNumberWithLeadingZero: function(number){
      if (number < 10){
        return "0" + number;
      }
      return number;
    },
    createDateNowWithTime: function(timeRepresentation) {
      var today = new Date().toISOString();
      var todaysDateRep = today.substring(0, today.indexOf('T') + 1);
      return new Date(todaysDateRep + timeRepresentation);
    },
    calculateDateFromDuration: function (beginDate, newDuration){
      var totalEndMinutes = beginDate.getMinutes() + (newDuration * 60);

      var newEndDate = new Date(beginDate.valueOf());
      newEndDate.setMinutes(totalEndMinutes);
      console.log("Total hours '" + newEndDate.getHours() + ", total minutes '" + newEndDate.getMinutes() + " (total '" + totalEndMinutes + "')");

      var newHoursAsString = this.getNumberWithLeadingZero(newEndDate.getHours());
      var newMinutesAsString = this.getNumberWithLeadingZero(newEndDate.getMinutes());

      var timeValueRepresentation = newHoursAsString + ":" + newMinutesAsString;
      console.log("New end-time snippet: '" + timeValueRepresentation + "'");
      var timeValueOject = new Object();
      timeValueOject.timeRepresentation = timeValueRepresentation;
      timeValueOject.timeValue = this.createDateNowWithTime(timeValueRepresentation);
      return timeValueOject;
    },
    beginOrEndTimeStampChanged: function(beginDate, endDate){
      var totalMinutesEndTimeStamp = 60 * endDate.getHours() + endDate.getMinutes();
      var totalMinutesBeginTimeStamp = 60 * beginDate.getHours() + beginDate.getMinutes();
      var hoursInBetween0 = (totalMinutesEndTimeStamp - totalMinutesBeginTimeStamp) / 60;
      var hoursInBetween = Math.round((hoursInBetween0 + Number.EPSILON) * 100) / 100;
      console.log("Time between changed begin and end time stamp: [" + hoursInBetween + " h]");
      return hoursInBetween;
    },
  }
}
