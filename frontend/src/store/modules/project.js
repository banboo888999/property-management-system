const state = {
  currentProjectId: localStorage.getItem('currentProjectId'),
  currentProject: JSON.parse(localStorage.getItem('currentProject') || 'null')
}

const mutations = {
  SET_CURRENT_PROJECT: (state, project) => {
    state.currentProjectId = project?.id
    state.currentProject = project
    
    // 持久化存储
    if (project) {
      localStorage.setItem('currentProjectId', project.id)
      localStorage.setItem('currentProject', JSON.stringify(project))
    } else {
      localStorage.removeItem('currentProjectId')
      localStorage.removeItem('currentProject')
    }
  }
}

const actions = {
  setCurrentProject({ commit }, project) {
    commit('SET_CURRENT_PROJECT', project)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}