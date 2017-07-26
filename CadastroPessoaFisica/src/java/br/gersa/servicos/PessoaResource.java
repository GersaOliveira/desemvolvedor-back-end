/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gersa.servicos;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import static javax.ws.rs.client.Entity.json;
import javax.ws.rs.core.MediaType;
import org.eclipse.persistence.internal.jaxb.json.schema.model.JsonSchema;

/**
 * REST Web Service
 *
 * @author gersa.oliveira
 */
@Path("pessoa")
public class PessoaResource {

    @Context
    private UriInfo context;
    Gson gson = new Gson();
    FileReader arq;
    BufferedReader lerArq;
    String linha;
    String arquivo = "C:\\cadastro\\cadastro";

    /**
     * Creates a new instance of GenericResource
     */
    private static List<pessoa> pessoalst;

    public PessoaResource() throws FileNotFoundException, IOException {

        pessoalst = new ArrayList<pessoa>();

        File file = new File(arquivo);

        if (!file.exists()) {

            pessoalst.add(new pessoa(41l, "Gersã", "gersa@gersa", "111111111"));
            pessoalst.add(new pessoa(42l, "Ana", "ana@ana", " 222222222"));
            pessoalst.add(new pessoa(43l, "Aline", "aline@aline", "333333333"));

            new File(arquivo).createNewFile();

            FileWriter fw = new FileWriter(arquivo, false);
            String aux = gson.toJson(pessoalst).toString();

            fw.write(aux);
            fw.close();

        } else {

            arq = new FileReader(arquivo);
            lerArq = new BufferedReader(arq);

            linha = lerArq.readLine();

            Gson json = new Gson();
            java.lang.reflect.Type collectionType = new TypeToken<List<pessoa>>() {
            }.getType();
            pessoalst = json.fromJson(linha.toString(), collectionType);
            arq.close();
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/consultaLista")
    public String getXml() {
        Gson gson = new Gson();

        return gson.toJson(pessoalst);

    }

    /**
     * PUT method for updating or creating an instance of PessoaResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Path("/atualizar/{pessoaId}/{nome}/{email}/{cpf}")
    public void putXml(@PathParam("pessoaId") int pessoaId, @PathParam("nome") String nome, @PathParam("email") String email, @PathParam("cpf") String cpf) {

        for (int i = 0; i < pessoalst.size(); i++) {

            if (pessoalst.get(i).getId() == pessoaId) {

                pessoalst.get(i).setCPF(cpf);
                pessoalst.get(i).setEmail(email);
                pessoalst.get(i).setNome(nome);

                Gson gson = new Gson();

                String aux = gson.toJson(pessoalst).toString();

                aux = linha.replace(linha, aux);
                try {
                    FileWriter fw = new FileWriter(arquivo, false);

                    fw.write(aux);
                    fw.close();

                } catch (Exception ex) {
                }

            }
        }
    }

    @DELETE
    @Path("/deletaPessoa/{pessoaId}")
    // @Produces(MediaType.APPLICATION_JSON)
    public boolean deletaPessoa(@PathParam("pessoaId") int pessoaId) {
        for (int i = 0; i < pessoalst.size(); i++) {
            if (pessoalst.get(i).getId() == pessoaId) {

                pessoalst.remove(i);
                String aux = gson.toJson(pessoalst).toString();

                aux = linha.replace(linha, aux);
                try {
                    FileWriter fw = new FileWriter(arquivo, false);

                    fw.write(aux);
                    fw.close();

                } catch (Exception ex) {
                }
                return true;
            }
        }
        return false;
    }

}
