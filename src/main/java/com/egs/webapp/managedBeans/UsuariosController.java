/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.egs.webapp.managedBeans;

import com.egs.webapp.entities.Estado;
import com.egs.webapp.entities.Rol;
import com.egs.webapp.entities.Usuario;
import com.egs.webapp.sessionBeans.EstadoFacade;
import com.egs.webapp.sessionBeans.UsuarioFacade;
import com.egs.webapp.util.JsfUtil;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author EduardoAlexis
 */
@Named(value = "usuariosController")
@SessionScoped
public class UsuariosController implements Serializable {

    // EJB sessionBeans 
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB 
    private EstadoFacade estadoFacade;
    
    
    @Inject
    RolController rolController;
    //attribute
    private String username;
    private Usuario selectedUser;
    private Usuario currentUser;
    private String pass;
    private List<Usuario> items = null;
    
    public UsuariosController() {
    }

    public UsuarioFacade getUsuarioFacade() {
        return usuarioFacade;
    }

    public void setUsuarioFacade(UsuarioFacade usuarioFacade) {
        this.usuarioFacade = usuarioFacade;
    }
    
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    
    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Usuario getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(Usuario selectedUser) {
        this.selectedUser = selectedUser;
    }
// current user in session
    public Usuario getCurrentUser() {
        if (currentUser==null){
        currentUser = new Usuario();
        currentUser = getUsuarioFacade().findUsuarioByUsername(getUsername());
    
        }
        
        return currentUser;
    }

    public void setCurrentUser(Usuario currentUser) {
        this.currentUser = currentUser;
    }

    public List<Usuario> getItems() {
        if (items == null) {

            // no tiene que encontrarlos a todos   
            items = getUsuarioFacade().usuariosActivos(getUsername());
        } 
    
     return items;
    
    }
    public void setItems(List<Usuario> items) {
        this.items = items;
    }
    

    
        public String login() {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context
                                     .getExternalContext().getRequest();
        try {
            request.login(username, pass);

        } catch (ServletException e) {

            FacesContext facesContext = FacesContext.getCurrentInstance();
            Flash flash = facesContext.getExternalContext().getFlash();
            flash.setKeepMessages(true);
            flash.setRedirect(true);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario y/o password incorrectos!", null));
            return "login";
        }

        if (request.isUserInRole("administrador")) {

//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            Flash flash = facesContext.getExternalContext().getFlash();
//            flash.setKeepMessages(true);
//            flash.setRedirect(true);
//            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "HOLA " + usuario.toUpperCase() + "!", "¿ Por donde empiezas hoy ?"));
             
            return "welcome-admin";
        } else if (request.isUserInRole("vendedor")) {

            return "login";
        } else {

            return "login";
        }

    }

    public String logout() {
        String result = "login";

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            request.logout();
            currentUser = null;
            
        } catch (ServletException e) {
            System.out.println("Error al intentar salir");
            result = "login";

        }

        return result;
    }
    //Navigation methods
    
    public String goWelcomeAdmin(){
    
    return "welcome-admin";
    }
   public String goUsuariosList(){
    
    return "usuarios-list";
    }
    
   public String goUsuarioEdit(){
    
    return "usuario-edit";
    }
   
   public String goUsuarioDelete(){
    
    return "usuario-delete";
    }
   
   public String goUsuarioCreate(){
        selectedUser = new Usuario();
        rolController.setSelected(new Rol());
    return "usuario-create";
    }
   
   public String create (){
        persistenciaUsuario(selectedUser, "El usuario se ha creado correctamente");
        if(!JsfUtil.isValidationFailed()){
          items = null;
         selectedUser = new Usuario();
         rolController.setSelected(new Rol());
         FacesContext facesContext = FacesContext.getCurrentInstance();
             Flash flash = facesContext.getExternalContext().getFlash();
             flash.setKeepMessages(true);
             flash.setRedirect(true);
             
        return "usuario-create";
        }
        return null;
    }
   
   
   
   public String goUsuarioView(){
    
    return "usuario-view";
    }
   
   public String goUsuariosDashboard(){
    
    return "usuarios-dashboard";
    }
  // create usuario 
   
    public String cadenaMd5 (String pass){
    
        try {
            String clave = pass;
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(clave.getBytes("UTF-8"), 0, clave.length());
            byte[] bt = md.digest();
            BigInteger bi = new BigInteger(1, bt);
            String md5 = bi.toString(16);
            return md5;
            
        
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(UsuariosController.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage(ex, "Lo sentimos ha ocurrido un error inesperado!");
            return null;
        } 
    
    
    }
   
    public void persistenciaUsuario (Usuario user,String msg){
        FacesContext context = FacesContext.getCurrentInstance(); 
        
        Usuario userAux = getUsuarioFacade().findUsuarioByUsername(user.getUsername());
       
        Estado estAux = estadoFacade.find(1);
        
        System.out.println("El estado es "+estAux.getDescripcion()+estAux.getIdEstado());
        user.setIdEstado(estAux);
        System.out.println("el valor de selected es "+user.getIdEstado());
        
        if (userAux==null ){
        userAux = new Usuario();
        
        userAux.setPass(cadenaMd5(user.getPass()));
        System.out.println("pass"+userAux.getPass());
        
        userAux.setApellidoPat(user.getApellidoPat());
        System.out.println("apellidoPat"+userAux.getApellidoPat());
        
        
        userAux.setApellidoMat(user.getApellidoMat());
        System.out.println("apellidoPat"+userAux.getApellidoMat());
        
        
        userAux.setNombre(user.getNombre());
        userAux.setMail(user.getMail());
        
        System.out.println("nombre"+userAux.getNombre());
        userAux.setUsername(user.getUsername());
        System.out.println("usuario"+userAux.getUsername());
        userAux.setIdRol(rolController.getSelected());
        System.out.println("rol id"+rolController.getSelected().getIdRol());
        userAux.setIdEstado(user.getIdEstado());
        System.out.println("estado"+userAux.getIdEstado());

        
        // para persistir en logMov
        Usuario usuarioEnSession = getUsuarioFacade().findUsuarioByUsername(username);
        
     
        
            try {
                if(userAux.getPass()!=null){
                getUsuarioFacade().create(userAux);
                
                JsfUtil.addSuccessMessage(msg);
                }else{JsfUtil.addErrorMessage("Error en la encriptacion de password!");}
            } catch (Exception e) {
                JsfUtil.addErrorMessage("Error al intentar crear un usuario!");
            }
        
        }else{JsfUtil.addErrorMessage("El usuario "+userAux.getUsername()+" ya existe o ya fue ocupado anteriormente!");}
        
        }
    
    
    public void llamarEditarUsuario(){
        
        if (selectedUser != null)  {
        
        editarUsuario(selectedUser, "editado Correctamente");
        
        } else{JsfUtil.addErrorMessage("Error al editar ");}
    }
        
   
    public void editarUsuario(Usuario currentUsuario, String successMessage) {
        if (currentUsuario != null) {
            
            try {
                getUsuarioFacade().edit(currentUsuario);
                JsfUtil.addSuccessMessage(successMessage);
            } 
             catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
   }
    
    

}       