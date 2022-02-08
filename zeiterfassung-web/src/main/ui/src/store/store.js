import {createStore} from 'vuex'

export const store = createStore({
  state: {
    businessDayHistoryOverviewDto: {
      businessDayHistoryDtos: [],
      ticketDistributionDto: {
        ticketDistributionDtoEntries: []
      }
    }
  },
  getters: {
    ticketDistributionChartData(state, getters) {
      let ticketDistributionsEntries = getters.ticketDistributionDtoEntries;
      return Array.from(ticketDistributionsEntries, ticketDistributionsEntry => [ticketDistributionsEntry.ticketNr, ticketDistributionsEntry.percentage]);
    },
    businessDayHistoryChartData(state, getters) {
      let businessDayHistoryDtos = getters.businessDayHistoryDtos;
      return Array.from(businessDayHistoryDtos, businessDayHistoryDto => [businessDayHistoryDto.dateRepresentation, businessDayHistoryDto.bookedHours]);
    },
    ticketDistributionDtoEntries: state => {
      return state.businessDayHistoryOverviewDto.ticketDistributionDto.ticketDistributionDtoEntries;
    },
    businessDayHistoryDtos: state => {
      return state.businessDayHistoryOverviewDto.businessDayHistoryDtos;
    }
  },
  mutations: {
    setBusinessDayHistoryOverviewDto(state, businessDayHistoryOverviewDto) {
      state.businessDayHistoryOverviewDto = businessDayHistoryOverviewDto
    },
  },
  actions: {
    setBusinessDayHistoryOverviewDto(context, businessDayHistoryOverviewDto) {
      context.commit("setBusinessDayHistoryOverviewDto", businessDayHistoryOverviewDto);
    },
  },
});
