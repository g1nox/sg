/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.egs.webapp.managedBeans;


import com.egs.webapp.entities.Rol;
import com.egs.webapp.entities.Usuario;
import com.egs.webapp.sessionBeans.UsuarioFacade;
import com.egs.webapp.util.JsfUtil;
import com.egs.webapp.util.JsfUtil.PersistAction;
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
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Named(value = "usuariosController")
@SessionScoped
public class UsuariosController implements Serializable {

    // EJB sessionBeans 
    @EJB
    private UsuarioFacade usuarioFacade;
 
    
    @Inject
    RolController rolController;
    //attribute
    private String username;
    private Usuario selectedUser;
    private Usuario currentUser;
    private String pass;
    
    private Usuario estadouser;
    
    private Usuario nombreuser;
    
    private List<Usuario> items = null;
    
    private List<Object> itemsTop = null;
    
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
//    public Usuario getCurrentUser() {
//        if (currentUser==null){
//        currentUser = new Usuario();
//        currentUser = getUsuarioFacade().findUsuarioByUsername(getUsername());
//    
//        }
//        
//        return currentUser;
//    }
    
    public Usuario getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Usuario currentUser) {
        this.currentUser = currentUser;
    }

    public List<Usuario> getItems() {
      if (items == null) {
            items = usuarioFacade.findAll();
        }
        return items;
    }
   
    public void setItems(List<Usuario> items) {
        this.items = items;
    }
    
     public List<Object> getItemsTop() {
        
        if (itemsTop == null) {
           itemsTop = getUsuarioFacade().meseroTop();
        }
        return itemsTop;
    }

    public Usuario getEstadouser() {
        
        estadouser = usuarioFacade.findUsuarioByUsername(username);
        
        return estadouser;
    }

    public void setEstadouser(Usuario estadouser) {
        this.estadouser = estadouser;
    }

    public Usuario getNombreuser() {
        return nombreuser;
    }

    public void setNombreuser(Usuario nombreuser) {
        this.nombreuser = nombreuser;
    }
    
     
     
        public String login() {
            
            if(currentUser == null){

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.login(username, pass);
            
            currentUser = new Usuario();
            currentUser = getUsuarioFacade().findUsuarioByUsername(getUsername());
            
              if (request.isUserInRole("administrador")) {

                return "welcome-admin";

            } else {
                
                JsfUtil.addErrorMessage("El usuario " + username + " se encuentra bloqueado");

            }

            if (request.isUserInRole("cajero") && getEstadouser().getEstado() == true) {

                return "welcome-cajero";
                
            } else {
                
                JsfUtil.addErrorMessage("El usuario " + username + " se encuentra bloqueado");

            }

            if (request.isUserInRole("mesero") && getEstadouser().getEstado() == true) {

                return "welcome-mesero";
                
            } else {
                
                JsfUtil.addErrorMessage("El usuario " + username + " se encuentra bloqueado");

            }

        } catch (ServletException e) {

            FacesContext facesContext = FacesContext.getCurrentInstance();
            Flash flash = facesContext.getExternalContext().getFlash();
            flash.setKeepMessages(true);
            flash.setRedirect(true);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario y/o password incorrectos!", null));
            return "login";
        }
        } else {
            
                if(currentUser.getIdRol().getIdRol() == 1){
                
                    return "welcome-admin";
                
                }
                
                if(currentUser.getIdRol().getIdRol() == 2 ){
                
                    return "welcome-cajero";
                
                }
                
                 if(currentUser.getIdRol().getIdRol() == 3 ){
                
                    return "welcome-mesero";
                
                }
            
            }
          

            return null;
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
       persist ( PersistAction.CREATE, "El usuario se ha creado correctamente");
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
   
       private void persist(PersistAction persistAction, String successMessage) {
        if (selectedUser != null) {
            
            try {
                if (persistAction == PersistAction.CREATE) {
                    
                    String nombre = getSelectedUser().getUsername();
                    nombreuser = usuarioFacade.findUsuarioByUsername(nombre);
                    
                    if (nombreuser == null) {
                        selectedUser.setPass(cadenaMd5(selectedUser.getPass()));
                        
                        getUsuarioFacade().edit(selectedUser);
                        
                        JsfUtil.addSuccessMessage(successMessage);
                    } else {
                        JsfUtil.addErrorMessage("El nombre de usuario ya existe");
                    }
                } 
                
                
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }
    
    
    public void llamarEditarUsuario(){
        
        if (selectedUser != null)  {
        
        editarUsuario(selectedUser, "editado Correctamente");
        
        } else { JsfUtil.addErrorMessage("Error al editar ");
      }
    }
        
   
    public void editarUsuario(Usuario currentUsuario, String successMessage) {
        if (currentUsuario != null) {
            
            try {
                currentUsuario.setPass(cadenaMd5(selectedUser.getPass()));
                
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