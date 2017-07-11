/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slGal.LiveEdu;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.InitialDirContext;

/**
 *
 * @author Андрей
 */
public class LDAP {

    private static String url = "ldap://192.168.0.107:389";
    private static String conntype = "simple";
    private static String AdminDn = "cn=admin,dc=ldap,dc=example,dc=com";
    private static String password = "admin";
    private static String UsersPath = ",cn=users,dc=ldap,dc=example,dc=com";

    static void addUser(String emailCorporate, String firstName, String lastName) throws NamingException {
        final Hashtable<Object, Object> env = new Hashtable<Object, Object>();
        DirContext dctx = null;
        try {
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, url);
            env.put(Context.SECURITY_AUTHENTICATION, conntype);
            env.put(Context.SECURITY_PRINCIPAL, AdminDn);
            env.put(Context.SECURITY_CREDENTIALS, password);
            dctx = new InitialDirContext(env);
            // Create a container set of attributes
            final Attributes container = new BasicAttributes();

            // Create the objectclass to add
            final Attribute objClasses = new BasicAttribute("objectClass");
            objClasses.add("inetOrgPerson");

            final Attribute employeeType = new BasicAttribute("employeeType");
            employeeType.add("test");

            // Assign the username, first name, and last name
            final Attribute email = new BasicAttribute("mail", "" + emailCorporate + "");
            final Attribute uid = new BasicAttribute("uid", "" + firstName + "_" + lastName + "");
            final Attribute surName = new BasicAttribute("sn", "" + lastName + "");

            // Add password
            final Attribute userPassword = new BasicAttribute("userpassword", "" + generatePassword() + "");

            // Add these to the container
            container.put(objClasses);
            container.put(employeeType);
            container.put(email);
            container.put(uid);
            container.put(surName);
            container.put(userPassword);

            // Create the entry
            dctx.createSubcontext(getUserDN("" + firstName + " " + lastName + ""), container);
        } finally {
            if (null != dctx) {
                try {
                    dctx.close();
                } catch (final NamingException e) {
                    System.out.println("Error in closing ldap " + e);
                }
            }
        }
    }

    static void DeleteUser(String firstName, String lastName) throws NamingException {
        final Hashtable<Object, Object> env = new Hashtable<Object, Object>();
        DirContext dctx = null;
        try {
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, url);
            env.put(Context.SECURITY_AUTHENTICATION, conntype);
            env.put(Context.SECURITY_PRINCIPAL, AdminDn);
            env.put(Context.SECURITY_CREDENTIALS, password);
            dctx = new InitialDirContext(env);
            dctx.destroySubcontext(getUserDN("" + firstName + " " + lastName + ""));
        } finally {
            if (null != dctx) {
                try {
                    dctx.close();
                } catch (final NamingException e) {
                    System.out.println("Error in closing ldap " + e);
                }
            }
        }
    }

    static void Modify(String firstName, String lastName, String type) throws NamingException {
        final Hashtable<Object, Object> env = new Hashtable<Object, Object>();
        DirContext dctx = null;
        try {
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, url);
            env.put(Context.SECURITY_AUTHENTICATION, conntype);
            env.put(Context.SECURITY_PRINCIPAL, AdminDn);
            env.put(Context.SECURITY_CREDENTIALS, password);
            dctx = new InitialDirContext(env);

            ModificationItem[] mods = new ModificationItem[1];

            Attribute mod = new BasicAttribute("employeeType", "" + type + "");

            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, mod);

            dctx.modifyAttributes(getUserDN("" + firstName + " " + lastName + ""), mods);
        } finally {
            if (null != dctx) {
                try {
                    dctx.close();
                } catch (final NamingException e) {
                    System.out.println("Error in closing ldap " + e);
                }
            }
        }
    }

    private static String getUserDN(final String userName) {
        String userDN = new StringBuffer().append("cn=").append(userName).append(UsersPath).toString();
        System.out.println(userDN);
        return userDN;
    }

    public static String generatePassword() {

        PasswordGenerator pswGen = new PasswordGenerator.BuilderMask()
                .appendMask(PasswordGenerator.SYMBOLS_UPPER_ALPHABETIC_ENGLISH, 1)
                .appendMask(PasswordGenerator.SYMBOLS_LOWER_ALPHABETIC_ENGLISH, 3)
                .appendMask(PasswordGenerator.SYMBOLS_DIGIT, 4)
                .build();

        return pswGen.generate();

    }

}
