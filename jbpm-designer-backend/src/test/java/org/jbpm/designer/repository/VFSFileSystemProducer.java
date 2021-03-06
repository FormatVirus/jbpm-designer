/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.jbpm.designer.repository;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.designer.repository.vfs.RepositoryDescriptor;
import org.uberfire.io.IOService;
import org.uberfire.io.impl.IOServiceDotFileImpl;
import org.uberfire.java.nio.file.FileSystem;
import org.uberfire.java.nio.file.Path;

public class VFSFileSystemProducer {

    private IOService ioService = new IOServiceDotFileImpl();

    public RepositoryDescriptor produceFileSystem( final Map<String, String> env ) {
        URI repositoryRoot = URI.create( env.get( "repository.root" ) );

        FileSystem fileSystem = ioService.getFileSystem( repositoryRoot );

        if ( fileSystem == null ) {

            fileSystem = ioService.newFileSystem( repositoryRoot, env );
        }

        // fetch file system changes - mainly for remote based file systems
        String fetchCommand = (String) env.get( "fetch.cmd" );
        if ( fetchCommand != null ) {
            fileSystem = ioService.getFileSystem( URI.create( env.get( "repository.root" ) + fetchCommand ) );
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put( env.get( "repository.root" ), "designer-repo" );

        Path rootPath = fileSystem.provider().getPath( repositoryRoot );
        return new RepositoryDescriptor( repositoryRoot, fileSystem, rootPath );
    }

    public IOService getIoService() {
        return this.ioService;
    }

}
